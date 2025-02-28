package com.monografia.EvoluTCC.services;

import com.monografia.EvoluTCC.models.Monografia;
import com.monografia.EvoluTCC.models.Usuario;
import com.monografia.EvoluTCC.producers.UserProducer;
import com.monografia.EvoluTCC.models.Especialidade;
import com.monografia.EvoluTCC.Enums.StatusMonografia;
import com.monografia.EvoluTCC.dto.MonografiaResponseDTO;
import com.monografia.EvoluTCC.repositories.MonografiaRepository;
import com.monografia.EvoluTCC.repositories.UsuarioRepository;

import jakarta.transaction.Transactional;

import com.monografia.EvoluTCC.repositories.EspecialidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Service
public class MonografiaService {

    @Autowired
    private MonografiaRepository monografiaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EspecialidadeRepository especialidadeRepository;

    @Autowired
    private UserProducer userProducer;

    // Cria uma nova monografia com status PENDENTE
    public Monografia createMonografia(String tema, MultipartFile extratoBancario, MultipartFile termoOrientador,
    MultipartFile declaracaoNotas, MultipartFile projeto, MultipartFile documentoBi,
    UUID alunoId, UUID orientadorId, UUID especialidadeId) throws IOException {

        boolean alunoJaPossuiMonografia = monografiaRepository.existsByAlunoId(alunoId);
        if (alunoJaPossuiMonografia) {
            throw new RuntimeException("O aluno já possui uma monografia cadastrada. Não é permitido inscrever-se novamente.");
        }
        
validarDocumento(extratoBancario);
validarDocumento(termoOrientador);
validarDocumento(declaracaoNotas);
validarDocumento(projeto);
validarDocumento(documentoBi);

// Busca o aluno
Usuario aluno = usuarioRepository.findById(alunoId)
.orElseThrow(() -> new RuntimeException("Aluno não encontrado com o ID: " + alunoId));

// Busca o orientador e valida se ele pertence à especialidade escolhida
Usuario orientador = usuarioRepository.findById(orientadorId)
.orElseThrow(() -> new RuntimeException("Orientador não encontrado com o ID: " + orientadorId));

if (!orientador.getEspecialidade().getId().equals(especialidadeId)) {
throw new RuntimeException("O orientador selecionado não pertence à especialidade escolhida.");
}

// Busca a especialidade
Especialidade especialidade = especialidadeRepository.findById(especialidadeId)
.orElseThrow(() -> new RuntimeException("Especialidade não encontrada com o ID: " + especialidadeId));

// Cria a monografia
Monografia monografia = new Monografia();
monografia.setTema(tema);
monografia.setExtratoBancario(extratoBancario.getBytes());
monografia.setTermoOrientador(termoOrientador.getBytes());
monografia.setDeclaracaoNotas(declaracaoNotas.getBytes());
monografia.setProjeto(projeto.getBytes());
monografia.setDocumentoBi(documentoBi.getBytes());
monografia.setStatus(StatusMonografia.PENDENTE);
monografia.setAluno(aluno);
monografia.setOrientador(orientador);
monografia.setEspecialidade(especialidade);
monografia.setDataStatus(LocalDateTime.now());

Monografia savedMonografia = monografiaRepository.save(monografia);

// Notifica o orientador
userProducer.notifyOrientador(savedMonografia);

return savedMonografia;
}

    // Atualiza a monografia com correções do aluno
    public Monografia updateMonografia(UUID id, String tema, MultipartFile extratoBancario, MultipartFile termoOrientador,
                                      MultipartFile declaracaoNotas, MultipartFile projeto, MultipartFile documentoBi) throws IOException {
        Monografia monografia = getMonografiaById(id);
        if (tema != null) monografia.setTema(tema);
        if (extratoBancario != null && !extratoBancario.isEmpty()) {
            validarDocumento(extratoBancario);
            monografia.setExtratoBancario(extratoBancario.getBytes());
        }
        if (termoOrientador != null && !termoOrientador.isEmpty()) {
            validarDocumento(termoOrientador);
            monografia.setTermoOrientador(termoOrientador.getBytes());
        }
        if (declaracaoNotas != null && !declaracaoNotas.isEmpty()) {
            validarDocumento(declaracaoNotas);
            monografia.setDeclaracaoNotas(declaracaoNotas.getBytes());
        }
        if (projeto != null && !projeto.isEmpty()) {
            validarDocumento(projeto);
            monografia.setProjeto(projeto.getBytes());
        }
        if (documentoBi != null && !documentoBi.isEmpty()) {
            validarDocumento(documentoBi);
            monografia.setDocumentoBi(documentoBi.getBytes());
        }

        // Altera o status para PENDENTE após correções
        monografia.setStatus(StatusMonografia.PENDENTE);
        Monografia updatedMonografia = monografiaRepository.save(monografia);

        // Notifica o orientador sobre a atualização
        userProducer.notifyOrientador(updatedMonografia);

        return updatedMonografia;
    }

    // Revisão do orientador (aprovação ou solicitação de correções)
    public Monografia reviewMonografia(UUID monografiaId, StatusMonografia novoStatus, String descricao, UUID orientadorId) {
        Monografia monografia = getMonografiaById(monografiaId);
        Usuario orientador = usuarioRepository.findById(orientadorId)
                .orElseThrow(() -> new RuntimeException("Orientador não encontrado com o ID: " + orientadorId));

        // Verifica se o usuário é realmente um Orientador
        if (!orientador.getTipoUsuario().getNome().equals("Orientador")) {
            throw new RuntimeException("Apenas usuários do tipo Orientador podem revisar monografias.");
        }

        // Verifica se o orientador tentando revisar é o mesmo selecionado pelo aluno
        if (!monografia.getOrientador().getId().equals(orientadorId)) {
            throw new RuntimeException("Apenas o orientador selecionado pelo aluno pode aprovar ou revisar esta monografia.");
        }

        // Impede o orientador de definir diretamente como DISPONIVEL
        if (novoStatus == StatusMonografia.DISPONIVEL) {
            throw new RuntimeException("O Orientador não pode definir a monografia como DISPONIVEL.");
        }

        if (novoStatus == StatusMonografia.APROVADO) {
            monografia.setStatus(StatusMonografia.APROVADO);
            // Notifica o admin
            userProducer.notifyAdmin(monografia);
        } else if (novoStatus == StatusMonografia.EM_REVISAO) {
            monografia.setStatus(StatusMonografia.EM_REVISAO);
            monografia.setDescricaoMelhoria(descricao);
            // Notifica o aluno sobre as correções necessárias
            userProducer.notifyAluno(monografia);
        }

        return monografiaRepository.save(monografia);
    }

    @Transactional
public MonografiaResponseDTO getMonografiaByAlunoId(UUID alunoId) {
    Monografia monografia = monografiaRepository.findByAlunoId(alunoId)
            .stream()
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Nenhuma monografia encontrada para o aluno com ID: " + alunoId));

    // Adiciona links para visualização dos documentos
    adicionarLinksDocumentos(monografia);

    // Converte a entidade Monografia para o DTO
    return toDTO(monografia);
}

private MonografiaResponseDTO toDTO(Monografia monografia) {
    MonografiaResponseDTO dto = new MonografiaResponseDTO();
    dto.setId(monografia.getId());
    dto.setTema(monografia.getTema());
    dto.setStatus(monografia.getStatus().toString()); 
    dto.setLinkExtratoBancario(monografia.getLinkExtratoBancario());
    dto.setLinkDeclaracaoNotas(monografia.getLinkDeclaracaoNotas());
    dto.setLinkTermoOrientador(monografia.getLinkTermoOrientador());
    dto.setLinkProjeto(monografia.getLinkProjeto());
    dto.setLinkDocumentoBi(monografia.getLinkDocumentoBi());
    Usuario orientador = monografia.getOrientador();
    String nomeCompleto = orientador.getNome() + " " + orientador.getSobrenome();
    dto.setOrientadorNomeCompleto(nomeCompleto);
    Especialidade especialidade = monografia.getEspecialidade();
    dto.setEspecialidade(especialidade.getNome()); 
    Usuario aluno = monografia.getAluno();
    String alunoNomeCompleto = aluno.getNome() + " " + aluno.getSobrenome();
    dto.setAlunoNomeCompleto(alunoNomeCompleto);

    return dto;
}

private void adicionarLinksDocumentos(Monografia monografia) {
    UUID monografiaId = monografia.getId();
    String baseUrl = "http://localhost:8082/monografias/" + monografiaId + "/documentos/";

    if (monografia.getExtratoBancario() != null && monografia.getExtratoBancario().length > 0) {
        monografia.setLinkExtratoBancario(baseUrl + "extrato_bancario/visualizar");
    }
    if (monografia.getDeclaracaoNotas() != null && monografia.getDeclaracaoNotas().length > 0) {
        monografia.setLinkDeclaracaoNotas(baseUrl + "declaracao_notas/visualizar");
    }
    if (monografia.getTermoOrientador() != null && monografia.getTermoOrientador().length > 0) {
        monografia.setLinkTermoOrientador(baseUrl + "termo_orientador/visualizar");
    }
    if (monografia.getProjeto() != null && monografia.getProjeto().length > 0) {
        monografia.setLinkProjeto(baseUrl + "projeto/visualizar");
    }
    if (monografia.getDocumentoBi() != null && monografia.getDocumentoBi().length > 0) {
        monografia.setLinkDocumentoBi(baseUrl + "documento_bi/visualizar");
    }
}

    // Revisão do admin (aprovação ou solicitação de correções)
    public Monografia adminReviewMonografia(UUID monografiaId, StatusMonografia novoStatus, String descricao, UUID adminId) {
        Monografia monografia = getMonografiaById(monografiaId);
        Usuario admin = usuarioRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + adminId));

        // Verifica se o usuário é um Admin
        if (!admin.getTipoUsuario().getNome().equals("Admin")) {
            throw new RuntimeException("Apenas usuários do tipo Admin podem aprovar monografias.");
        }

        // Verifica se a monografia foi aprovada pelo orientador
        if (monografia.getStatus() != StatusMonografia.APROVADO) {
            throw new RuntimeException("A monografia precisa ser aprovada pelo orientador antes de ser revisada pelo admin.");
        }

        if (novoStatus == StatusMonografia.DISPONIVEL) {
            monografia.setStatus(StatusMonografia.DISPONIVEL);
            monografia.setAdmin(admin);
        } else if (novoStatus == StatusMonografia.EM_REVISAO) {
            monografia.setStatus(StatusMonografia.EM_REVISAO);
            monografia.setDescricaoMelhoria(descricao);
            monografia.setAdmin(admin);
            // Notifica o aluno sobre as correções necessárias
            userProducer.notifyAluno(monografia);
        }

        return monografiaRepository.save(monografia);
    }

    // Método para o Admin visualizar monografias aprovadas pelos orientadores
    public List<Monografia> getMonografiasAprovadas() {
        return monografiaRepository.findByStatus(StatusMonografia.APROVADO);
    }

    // Métodos auxiliares
    @SuppressWarnings("null")
    private void validarDocumento(MultipartFile documento) {
        if (documento != null && !documento.isEmpty()) {
            if (!documento.getContentType().equals("application/pdf")) {
                throw new IllegalArgumentException("O documento " + documento.getOriginalFilename() + " deve ser no formato PDF.");
            }
            if (documento.getSize() > 5 * 1024 * 1024) { // 5MB
                throw new IllegalArgumentException("O documento " + documento.getOriginalFilename() + " deve ter no máximo 5MB.");
            }
        }
    }
    
    public Monografia getMonografiaById(UUID id) {
        return monografiaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Monografia não encontrada com o ID: " + id));
    }

    public ResponseEntity<byte[]> getDocumento(UUID monografiaId, String tipoDocumento, String acao) {
        Monografia monografia = getMonografiaById(monografiaId);
        byte[] documento = null;
        String nomeArquivo = tipoDocumento + ".pdf";

        switch (tipoDocumento.toLowerCase()) {
            case "extrato_bancario": documento = monografia.getExtratoBancario(); break;
            case "termo_orientador": documento = monografia.getTermoOrientador(); break;
            case "declaracao_notas": documento = monografia.getDeclaracaoNotas(); break;
            case "projeto": documento = monografia.getProjeto(); break;
            case "documento_bi": documento = monografia.getDocumentoBi(); break;
            default: throw new IllegalArgumentException("Tipo de documento inválido: " + tipoDocumento);
        }

        if (documento == null || documento.length == 0) {
            throw new RuntimeException("Documento não encontrado para o tipo: " + tipoDocumento);
        }

        String contentDisposition = "inline";
        if ("baixar".equalsIgnoreCase(acao)) {
            contentDisposition = "attachment";
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition + "; filename=" + nomeArquivo)
                .contentType(MediaType.APPLICATION_PDF)
                .body(documento);
    }

    public List<Monografia> getMonografiasPorOrientador(UUID orientadorId) {
        if (!usuarioRepository.existsById(orientadorId)) {
            throw new RuntimeException("Orientador não encontrado com o ID: " + orientadorId);
        }
        return monografiaRepository.findByOrientadorId(orientadorId);
    }

    @Transactional
    public Map<String, Object> getEstatisticasAluno(UUID alunoId) {
        Map<String, Object> estatisticas = new HashMap<>();
    
        // Busca todas as monografias do aluno
        List<Monografia> monografias = monografiaRepository.findByAlunoId(alunoId);
    
        if (monografias.isEmpty()) {
            throw new RuntimeException("Nenhuma monografia encontrada para o aluno com ID: " + alunoId);
        }
    
        // Pega a última monografia (assumindo que o aluno tem apenas uma monografia ativa)
        Monografia ultimaMonografia = monografias.get(monografias.size() - 1);
    
        // Status atual da monografia
        estatisticas.put("statusAtual", ultimaMonografia.getStatus());
    
        // Tempo no status atual (em dias)
        long diasNoStatus = calcularDiasNoStatus(ultimaMonografia);
        estatisticas.put("diasNoStatus", diasNoStatus);
    
        // Número de revisões realizadas
        long numeroRevisoes = monografias.stream()
                .filter(m -> m.getStatus() == StatusMonografia.EM_REVISAO)
                .count();
        estatisticas.put("numeroRevisoes", numeroRevisoes);
    
        // Documentos pendentes
        List<String> documentosPendentes = verificarDocumentosPendentes(ultimaMonografia);
        estatisticas.put("documentosPendentes", documentosPendentes);
    
        // Tempo médio de revisão (em dias)
        double tempoMedioRevisao = calcularTempoMedioRevisao(monografias);
        estatisticas.put("tempoMedioRevisao", tempoMedioRevisao);
    
        // Chance de aprovação (exemplo simplificado)
        double chanceAprovacao = calcularChanceAprovacao(ultimaMonografia);
        estatisticas.put("chanceAprovacao", chanceAprovacao + "%");
    
        return estatisticas;
    }

// Método auxiliar para calcular dias no status atual
private long calcularDiasNoStatus(Monografia monografia) {
    if (monografia.getStatus() == StatusMonografia.PENDENTE || monografia.getStatus() == StatusMonografia.EM_REVISAO) {
        LocalDateTime dataAtual = LocalDateTime.now();
        LocalDateTime dataStatus = monografia.getDataStatus(); // Supondo que você tenha um campo para armazenar a data do último status
        return ChronoUnit.DAYS.between(dataStatus, dataAtual);
    }
    return 0;
}

// Método auxiliar para verificar documentos pendentes
private List<String> verificarDocumentosPendentes(Monografia monografia) {
    List<String> documentosPendentes = new ArrayList<>();
    if (monografia.getExtratoBancario() == null || monografia.getExtratoBancario().length == 0) {
        documentosPendentes.add("Extrato Bancário");
    }
    if (monografia.getTermoOrientador() == null || monografia.getTermoOrientador().length == 0) {
        documentosPendentes.add("Termo de Orientador");
    }
    if (monografia.getDeclaracaoNotas() == null || monografia.getDeclaracaoNotas().length == 0) {
        documentosPendentes.add("Declaração de Notas");
    }
    if (monografia.getProjeto() == null || monografia.getProjeto().length == 0) {
        documentosPendentes.add("Projeto");
    }
    if (monografia.getDocumentoBi() == null || monografia.getDocumentoBi().length == 0) {
        documentosPendentes.add("Documento de Identificação");
    }
    return documentosPendentes;
}

// Método auxiliar para calcular o tempo médio de revisão
private double calcularTempoMedioRevisao(List<Monografia> monografias) {
    return monografias.stream()
            .filter(m -> m.getStatus() == StatusMonografia.EM_REVISAO)
            .mapToLong(this::calcularDiasNoStatus)
            .average()
            .orElse(0);
}

// Método auxiliar para calcular a chance de aprovação (exemplo simplificado)
private double calcularChanceAprovacao(Monografia monografia) {
    if (monografia.getStatus() == StatusMonografia.APROVADO) {
        return 100.0;
    }
    if (monografia.getStatus() == StatusMonografia.EM_REVISAO) {
        return 70.0; // Exemplo: 70% de chance se estiver em revisão
    }
    return 30.0; // Exemplo: 30% de chance se estiver pendente
}
    
}