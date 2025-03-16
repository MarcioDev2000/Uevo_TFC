package com.monografia.EvoluTCC.services;

import com.monografia.EvoluTCC.models.Monografia;
import com.monografia.EvoluTCC.models.Usuario;
import com.monografia.EvoluTCC.producers.UserProducer;
import com.monografia.EvoluTCC.models.Curso;
import com.monografia.EvoluTCC.models.Especialidade;
import com.monografia.EvoluTCC.Enums.StatusMonografia;
import com.monografia.EvoluTCC.dto.AlunoResponseDTO;
import com.monografia.EvoluTCC.dto.MonografiaResponseDTO;
import com.monografia.EvoluTCC.repositories.MonografiaRepository;
import com.monografia.EvoluTCC.repositories.UsuarioRepository;
import com.monografia.EvoluTCC.Enums.TipoUsuario;
import jakarta.transaction.Transactional;

import com.monografia.EvoluTCC.repositories.CursoRepository;
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
import java.util.stream.Collectors;


@Service
public class MonografiaService {

    @Autowired
    private MonografiaRepository monografiaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private EspecialidadeRepository especialidadeRepository;

    @Autowired
    private UserProducer userProducer;

    public Monografia createMonografia(String tema, MultipartFile extratoBancario, MultipartFile termoOrientador,
                                  MultipartFile declaracaoNotas, MultipartFile projeto, MultipartFile documentoBi,
                                  MultipartFile termoDoAluno,
                                  UUID alunoId, UUID orientadorId, UUID especialidadeId, UUID cursoId) throws IOException {

    // Verifica se o curso existe
    Curso curso = cursoRepository.findById(cursoId)
            .orElseThrow(() -> new RuntimeException("Curso não encontrado com o ID: " + cursoId));

    // Verifica se o aluno já possui uma monografia cadastrada
    boolean alunoJaPossuiMonografia = monografiaRepository.existsByAlunoId(alunoId);
    if (alunoJaPossuiMonografia) {
        throw new RuntimeException("O aluno já possui uma monografia cadastrada. Não é permitido inscrever-se novamente.");
    }

    // Verifica se o aluno já está associado a outro orientador
    boolean alunoJaTemOrientador = monografiaRepository.existsByAlunoIdAndOrientadorIdNot(alunoId, orientadorId);
    if (alunoJaTemOrientador) {
        throw new RuntimeException("O aluno já está associado a outro orientador. Não é permitido alterar o orientador.");
    }

    long alunosOrientados = monografiaRepository.countByOrientadorId(orientadorId);
    if (alunosOrientados >= 10) {
        throw new RuntimeException("O orientador já atingiu o limite de 10 alunos. Escolha outro orientador.");
    }

    // Valida os documentos
    validarDocumento(extratoBancario);
    validarDocumento(termoOrientador);
    validarDocumento(declaracaoNotas);
    validarDocumento(projeto);
    validarDocumento(documentoBi);
    validarDocumento(termoDoAluno);

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
    monografia.setTermoDoAluno(termoDoAluno.getBytes()); 
    monografia.setStatus(StatusMonografia.PENDENTE);
    monografia.setAluno(aluno);
    monografia.setOrientador(orientador);
    monografia.setEspecialidade(especialidade);
    monografia.setCurso(curso); // Adiciona o curso
    monografia.setDataStatus(LocalDateTime.now());

    Monografia savedMonografia = monografiaRepository.save(monografia);

    // Notifica o orientador
    userProducer.notifyOrientador(savedMonografia);

    return savedMonografia;
}

public Monografia updateMonografia(UUID id, String tema, MultipartFile extratoBancario, MultipartFile termoOrientador,
MultipartFile declaracaoNotas, MultipartFile projeto, MultipartFile documentoBi, 
MultipartFile termoDoAluno, UUID cursoId) throws IOException {
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
if (termoDoAluno != null && !termoDoAluno.isEmpty()) {
validarDocumento(termoDoAluno);
monografia.setTermoDoAluno(termoDoAluno.getBytes());
}

// Atualiza o curso, se necessário
if (cursoId != null) {
Curso curso = cursoRepository.findById(cursoId)
.orElseThrow(() -> new RuntimeException("Curso não encontrado com o ID: " + cursoId));
monografia.setCurso(curso);
}

// Altera o status para PENDENTE após correções
monografia.setStatus(StatusMonografia.PENDENTE);
Monografia updatedMonografia = monografiaRepository.save(monografia);

// Notifica o orientador sobre a atualização
userProducer.notifyOrientador(updatedMonografia);

return updatedMonografia;
}

   
    public Monografia reviewMonografia(UUID monografiaId, StatusMonografia novoStatus, String descricao, UUID orientadorId) {
        Monografia monografia = getMonografiaById(monografiaId);
        Usuario orientador = usuarioRepository.findById(orientadorId)
                .orElseThrow(() -> new RuntimeException("Orientador não encontrado com o ID: " + orientadorId));
    
        // Verifica se o usuário é realmente um Orientador
        if (!"Orientador".equals(orientador.getTipoUsuario().getNome())) {
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
    
        // Se a monografia estiver sendo aprovada, associamos um Admin se necessário
        if (novoStatus == StatusMonografia.APROVADO) {
            monografia.setStatus(StatusMonografia.APROVADO);
            monografia.setAprovadoPor(TipoUsuario.ORIENTADOR);
            if (monografia.getAdmin() == null) {
                List<Usuario> admins = usuarioRepository.findByTipoUsuario_Nome("Admin");
                Usuario admin = admins.stream().findFirst()
                        .orElseThrow(() -> new RuntimeException("Nenhum admin encontrado no sistema."));
                monografia.setAdmin(admin);
            }
    
            monografiaRepository.save(monografia);
            userProducer.notifyAdmin(monografia);
        } else if (novoStatus == StatusMonografia.EM_REVISAO) {
            monografia.setStatus(StatusMonografia.EM_REVISAO);
            monografia.setDescricaoMelhoria(descricao);
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
        dto.setLinkTermoDoAluno(monografia.getLinkTermoDoAluno());
        dto.setDescricaoMelhoria(monografia.getDescricaoMelhoria());
    
        Usuario orientador = monografia.getOrientador();
        String nomeCompleto = orientador.getNome() + " " + orientador.getSobrenome();
        dto.setOrientadorNomeCompleto(nomeCompleto);
    
        Especialidade especialidade = monografia.getEspecialidade();
        dto.setEspecialidade(especialidade.getNome());
    
        Usuario aluno = monografia.getAluno();
        String alunoNomeCompleto = aluno.getNome() + " " + aluno.getSobrenome();
        dto.setAlunoNomeCompleto(alunoNomeCompleto);
    
        // Adiciona o nome do curso
        dto.setCursoNome(monografia.getCurso().getNome());
    
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

    if (monografia.getTermoDoAluno() != null && monografia.getTermoDoAluno().length > 0) {
        monografia.setLinkTermoDoAluno(baseUrl + "termo_do_aluno/visualizar");
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
            monografia.setAprovadoPor(TipoUsuario.ADMIN); // Define que foi aprovado por um Admin
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
            case "termo_do_aluno": documento = monografia.getTermoDoAluno(); break;
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

     public MonografiaService(MonografiaRepository monografiaRepository, UsuarioRepository usuarioRepository) {
        this.monografiaRepository = monografiaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
public List<MonografiaResponseDTO> getMonografiasPorOrientador(UUID orientadorId) {
    if (!usuarioRepository.existsById(orientadorId)) {
        throw new RuntimeException("Orientador não encontrado com o ID: " + orientadorId);
    }

    List<Monografia> monografias = monografiaRepository.findByOrientadorId(orientadorId);

    // Adiciona links para os documentos de cada monografia
    monografias.forEach(this::adicionarLinksDocumentos);

    // Mapeia a lista de Monografia para MonografiaResponseDTO
    return monografias.stream()
            .map(this::mapToMonografiaResponseDTO)
            .collect(Collectors.toList());
}

    private MonografiaResponseDTO mapToMonografiaResponseDTO(Monografia monografia) {
        MonografiaResponseDTO dto = new MonografiaResponseDTO();
        dto.setId(monografia.getId());
        dto.setTema(monografia.getTema());
        dto.setStatus(monografia.getStatus().toString());
        dto.setLinkExtratoBancario(monografia.getLinkExtratoBancario());
        dto.setLinkDeclaracaoNotas(monografia.getLinkDeclaracaoNotas());
        dto.setLinkTermoOrientador(monografia.getLinkTermoOrientador());
        dto.setLinkProjeto(monografia.getLinkProjeto());
        dto.setLinkDocumentoBi(monografia.getLinkDocumentoBi());
        dto.setDescricaoMelhoria(monografia.getDescricaoMelhoria()); // Mapeia a descrição
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

    @Transactional
public Map<String, Object> getEstatisticasAluno(UUID alunoId) {
    Map<String, Object> estatisticas = new HashMap<>();

    // Busca todas as monografias do aluno
    List<Monografia> monografias = monografiaRepository.findByAlunoId(alunoId);

    if (monografias.isEmpty()) {
        throw new RuntimeException("Nenhuma monografia encontrada para o aluno com ID: " + alunoId);
    }

    Monografia ultimaMonografia = monografias.get(monografias.size() - 1);

    // Adiciona o status atual da monografia
    estatisticas.put("statusAtual", ultimaMonografia.getStatus());

    // Calcula os dias no status atual
    long diasNoStatus = calcularDiasNoStatus(ultimaMonografia);
    estatisticas.put("diasNoStatus", diasNoStatus);

    // Calcula o número de revisões
    long numeroRevisoes = monografias.stream()
            .filter(m -> m.getStatus() == StatusMonografia.EM_REVISAO)
            .count();
    estatisticas.put("numeroRevisoes", numeroRevisoes);

    // Verifica documentos pendentes
    List<String> documentosPendentes = verificarDocumentosPendentes(ultimaMonografia);
    estatisticas.put("documentosPendentes", documentosPendentes);

    // Calcula o tempo médio de revisão
    double tempoMedioRevisao = calcularTempoMedioRevisao(monografias);
    estatisticas.put("tempoMedioRevisao", tempoMedioRevisao);

    // Calcula a chance de aprovação
    double chanceAprovacao = calcularChanceAprovacao(ultimaMonografia);
    estatisticas.put("chanceAprovacao", chanceAprovacao + "%");

    // Adiciona o status EM_PRE_DEFESA às estatísticas
    long emPreDefesa = monografias.stream()
            .filter(m -> m.getStatus() == StatusMonografia.EM_PRE_DEFESA)
            .count();
    estatisticas.put("monografiasEmPreDefesa", emPreDefesa);

    return estatisticas;
}


private long calcularDiasNoStatus(Monografia monografia) {
    if (monografia.getStatus() == StatusMonografia.PENDENTE || 
        monografia.getStatus() == StatusMonografia.EM_REVISAO || 
        monografia.getStatus() == StatusMonografia.EM_PRE_DEFESA) { // Adiciona o status EM_PRE_DEFESA
        LocalDateTime dataAtual = LocalDateTime.now();
        LocalDateTime dataStatus = monografia.getDataStatus(); 
        return ChronoUnit.DAYS.between(dataStatus, dataAtual);
    }
    return 0;
}

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


private double calcularTempoMedioRevisao(List<Monografia> monografias) {
    return monografias.stream()
            .filter(m -> m.getStatus() == StatusMonografia.EM_REVISAO)
            .mapToLong(this::calcularDiasNoStatus)
            .average()
            .orElse(0);
}


private double calcularChanceAprovacao(Monografia monografia) {
    if (monografia.getStatus() == StatusMonografia.APROVADO) {
        return 100.0;
    }
    if (monografia.getStatus() == StatusMonografia.EM_REVISAO) {
        return 70.0; 
    }
    return 30.0;
}


@Transactional
public Map<String, Integer> getEstatisticasStatusPorAlunoId(UUID alunoId) {
    Map<String, Integer> estatisticas = new HashMap<>();

    // Inicializa os contadores
    estatisticas.put("Pendente", 0);
    estatisticas.put("Aprovado", 0);
    estatisticas.put("Em_Revisao", 0);
    estatisticas.put("Em_Pre_Defesa", 0); // Adiciona o status EM_PRE_DEFESA

    // Busca a monografia do aluno
    Monografia monografia = monografiaRepository.findByAlunoId(alunoId)
            .stream()
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Nenhuma monografia encontrada para o aluno com ID: " + alunoId));

    // Atualiza as estatísticas com base no status atual da monografia
    switch (monografia.getStatus()) {
        case PENDENTE:
            estatisticas.put("Pendente", 1);
            break;
        case APROVADO:
            estatisticas.put("Aprovado", 1);
            break;
        case EM_REVISAO:
            estatisticas.put("Em_Revisao", 1);
            break;
        case EM_PRE_DEFESA:
            estatisticas.put("Em_Pre_Defesa", 1); // Adiciona o status EM_PRE_DEFESA
            break;
        default:
            throw new RuntimeException("Status da monografia inválido: " + monografia.getStatus());
    }

    return estatisticas;
}

public Monografia entregarProjetoParaPreDefesa(UUID monografiaId, MultipartFile projeto) throws IOException {
    Monografia monografia = getMonografiaById(monografiaId);

    // Verifica se a monografia está aprovada pelo orientador
    if (monografia.getStatus() != StatusMonografia.APROVADO) {
        throw new RuntimeException("A monografia precisa ser aprovada pelo orientador antes de entregar o projeto para a pré-defesa.");
    }

    // Valida o documento do projeto
    validarDocumento(projeto);

    // Atualiza o projeto na monografia
    monografia.setProjeto(projeto.getBytes());
    monografia.setStatus(StatusMonografia.EM_PRE_DEFESA); 

    return monografiaRepository.save(monografia);
}

public List<Monografia> getMonografiasEmPreDefesa() {
    return monografiaRepository.findByStatus(StatusMonografia.EM_PRE_DEFESA);
}

@Transactional
public Map<String, Object> getEstatisticasPorOrientador(UUID orientadorId) {
    // Verifica se o orientador existe
    Usuario orientador = usuarioRepository.findById(orientadorId)
            .orElseThrow(() -> new RuntimeException("Orientador não encontrado com o ID: " + orientadorId));

    if (!orientador.getTipoUsuario().getNome().equals("Orientador")) {
        throw new RuntimeException("O usuário com ID " + orientadorId + " não é um orientador.");
    }

    // Busca as monografias do orientador
    List<Monografia> monografias = monografiaRepository.findByOrientadorId(orientadorId);

    // Cria o mapa de estatísticas
    Map<String, Object> estatisticas = new HashMap<>();
    estatisticas.put("orientadorNome", orientador.getNome() + " " + orientador.getSobrenome());
    estatisticas.put("numeroAlunosOrientados", monografias.stream().map(Monografia::getAluno).distinct().count());

    // Calcula o número de monografias em cada status
    long pendentes = monografias.stream().filter(m -> m.getStatus() == StatusMonografia.PENDENTE).count();
    long emRevisao = monografias.stream().filter(m -> m.getStatus() == StatusMonografia.EM_REVISAO).count();
    long aprovadas = monografias.stream().filter(m -> m.getStatus() == StatusMonografia.APROVADO).count();
    long emPreDefesa = monografias.stream().filter(m -> m.getStatus() == StatusMonografia.EM_PRE_DEFESA).count(); // Adiciona o status EM_PRE_DEFESA

    // Adiciona as estatísticas ao mapa
    estatisticas.put("monografiasPendentes", pendentes);
    estatisticas.put("monografiasEmRevisao", emRevisao);
    estatisticas.put("monografiasAprovadas", aprovadas);
    estatisticas.put("monografiasEmPreDefesa", emPreDefesa); // Adiciona o status EM_PRE_DEFESA

    return estatisticas;
}

@Transactional
public List<AlunoResponseDTO> getAlunosPorOrientador(UUID orientadorId) {
    if (!usuarioRepository.existsById(orientadorId)) {
        throw new RuntimeException("Orientador não encontrado com o ID: " + orientadorId);
    }

    List<Monografia> monografias = monografiaRepository.findByOrientadorId(orientadorId);

    return monografias.stream()
            .map(Monografia::getAluno) // Obtém o aluno de cada monografia
            .distinct() // Remove alunos duplicados
            .map(this::mapToAlunoResponseDTO) // Mapeia para AlunoResponseDTO
            .collect(Collectors.toList());
}

// Método auxiliar para mapear Aluno para AlunoResponseDTO
private AlunoResponseDTO mapToAlunoResponseDTO(Usuario aluno) {
    AlunoResponseDTO dto = new AlunoResponseDTO();
    dto.setNome(aluno.getNome());
    dto.setSobrenome(aluno.getSobrenome());
    dto.setEndereco(aluno.getEndereco());
    dto.setTelefone(aluno.getTelefone());
    dto.setEmail(aluno.getEmail());
    dto.setNif(aluno.getNif());
    dto.setMatricula(aluno.getMatricula());
    dto.setCurso(aluno.getCurso() != null ? aluno.getCurso().getNome() : null); // Extrai o nome do curso
    return dto;
}

@Transactional
public MonografiaResponseDTO getMonografiaByOrientadorId(UUID orientadorId, UUID monografiaId) {
    // Verifica se o orientador existe
    Usuario orientador = usuarioRepository.findById(orientadorId)
            .orElseThrow(() -> new RuntimeException("Orientador não encontrado com o ID: " + orientadorId));

    // Verifica se o usuário é realmente um Orientador
    if (!"Orientador".equals(orientador.getTipoUsuario().getNome())) {
        throw new RuntimeException("O usuário com ID " + orientadorId + " não é um orientador.");
    }

    // Busca a monografia pelo ID e verifica se pertence ao orientador
    Monografia monografia = monografiaRepository.findById(monografiaId)
            .orElseThrow(() -> new RuntimeException("Monografia não encontrada com o ID: " + monografiaId));

    if (!monografia.getOrientador().getId().equals(orientadorId)) {
        throw new RuntimeException("A monografia não pertence ao orientador com ID: " + orientadorId);
    }

    adicionarLinksDocumentos(monografia);

    return toDTO(monografia);
}


@Transactional
public Map<String, Object> getEstatisticasAdmin(UUID adminId) {
    // Verifica se o admin existe
    Usuario admin = usuarioRepository.findById(adminId)
            .orElseThrow(() -> new RuntimeException("Admin não encontrado com o ID: " + adminId));

    // Verifica se o usuário é realmente um Admin
    if (!"Admin".equals(admin.getTipoUsuario().getNome())) {
        throw new RuntimeException("O usuário com ID " + adminId + " não é um admin.");
    }

    Map<String, Object> estatisticas = new HashMap<>();

    // Total de usuários do tipo Aluno
    long totalAlunos = usuarioRepository.countByTipoUsuarioNome("Aluno");
    estatisticas.put("totalAlunos", totalAlunos);

    // Total de usuários do tipo Orientador
    long totalOrientadores = usuarioRepository.countByTipoUsuarioNome("Orientador");
    estatisticas.put("totalOrientadores", totalOrientadores);

    // Total de monografias aprovadas
    long totalMonografiasAprovadas = monografiaRepository.countByStatus(StatusMonografia.APROVADO);
    estatisticas.put("totalMonografiasAprovadas", totalMonografiasAprovadas);

    // Total de monografias em revisão
    long totalMonografiasEmRevisao = monografiaRepository.countByStatus(StatusMonografia.EM_REVISAO);
    estatisticas.put("totalMonografiasEmRevisao", totalMonografiasEmRevisao);

    long totalMonografiasEmPreDefesa = monografiaRepository.countByStatus(StatusMonografia.EM_PRE_DEFESA);
    estatisticas.put("totalMonografiasEmPreDefesa", totalMonografiasEmPreDefesa);

    return estatisticas;
}

@Transactional
public List<MonografiaResponseDTO> listarMonografiasAprovadasPorAdmin(UUID adminId) {
    // Verifica se o admin existe
    Usuario admin = usuarioRepository.findById(adminId)
            .orElseThrow(() -> new RuntimeException("Admin não encontrado com o ID: " + adminId));

    // Verifica se o usuário é realmente um Admin
    if (!"Admin".equals(admin.getTipoUsuario().getNome())) {
        throw new RuntimeException("O usuário com ID " + adminId + " não é um admin.");
    }

    // Busca todas as monografias aprovadas
    List<Monografia> monografiasAprovadas = monografiaRepository.findByStatus(StatusMonografia.APROVADO);

    // Filtra as monografias aprovadas por Orientador (ignora as aprovadas por Admin)
    monografiasAprovadas = monografiasAprovadas.stream()
        .filter(monografia -> monografia.getAprovadoPor() == TipoUsuario.ORIENTADOR) // Apenas monografias aprovadas por Orientador
        .collect(Collectors.toList());

    // Adiciona links para os documentos de cada monografia
    monografiasAprovadas.forEach(this::adicionarLinksDocumentos);

    // Mapeia a lista de Monografia para MonografiaResponseDTO
    return monografiasAprovadas.stream()
            .map(this::mapToMonografiaResponseDTO)
            .collect(Collectors.toList());
}

@Transactional
public MonografiaResponseDTO getMonografiaAprovadaPorAdmin(UUID adminId, UUID monografiaId) {
    // Verifica se o admin existe
    Usuario admin = usuarioRepository.findById(adminId)
            .orElseThrow(() -> new RuntimeException("Admin não encontrado com o ID: " + adminId));

    // Verifica se o usuário é realmente um Admin
    if (!"Admin".equals(admin.getTipoUsuario().getNome())) {
        throw new RuntimeException("O usuário com ID " + adminId + " não é um admin.");
    }

    // Busca a monografia pelo ID
    Monografia monografia = monografiaRepository.findById(monografiaId)
            .orElseThrow(() -> new RuntimeException("Monografia não encontrada com o ID: " + monografiaId));

    // Verifica se a monografia está aprovada
    if (monografia.getStatus() != StatusMonografia.APROVADO) {
        throw new RuntimeException("A monografia com ID " + monografiaId + " não está aprovada.");
    }

    // Adiciona links para os documentos da monografia
    adicionarLinksDocumentos(monografia);

    // Converte a entidade Monografia para o DTO
    return mapToMonografiaResponseDTO(monografia);
}

    
}