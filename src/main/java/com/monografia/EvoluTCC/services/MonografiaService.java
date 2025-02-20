package com.monografia.EvoluTCC.services;

import com.monografia.EvoluTCC.models.Monografia;
import com.monografia.EvoluTCC.models.Usuario;
import com.monografia.EvoluTCC.producers.UserProducer;
import com.monografia.EvoluTCC.models.Especialidade;
import com.monografia.EvoluTCC.Enums.StatusMonografia;
import com.monografia.EvoluTCC.repositories.MonografiaRepository;
import com.monografia.EvoluTCC.repositories.UsuarioRepository;
import com.monografia.EvoluTCC.repositories.EspecialidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.io.IOException;
import java.util.List;
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
        validarDocumento(extratoBancario);
        validarDocumento(termoOrientador);
        validarDocumento(declaracaoNotas);
        validarDocumento(projeto);
        validarDocumento(documentoBi);

        Usuario aluno = usuarioRepository.findById(alunoId)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado com o ID: " + alunoId));

        Usuario orientador = usuarioRepository.findById(orientadorId)
                .orElseThrow(() -> new RuntimeException("Orientador não encontrado com o ID: " + orientadorId));

        Especialidade especialidade = especialidadeRepository.findById(especialidadeId)
                .orElseThrow(() -> new RuntimeException("Especialidade não encontrada com o ID: " + especialidadeId));

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
        if (documento != null && !documento.isEmpty() && !documento.getContentType().equals("application/pdf")) {
            throw new IllegalArgumentException("O documento " + documento.getOriginalFilename() + " deve ser no formato PDF.");
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
    
}