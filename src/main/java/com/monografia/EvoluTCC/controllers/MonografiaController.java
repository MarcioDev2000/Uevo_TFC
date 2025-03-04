package com.monografia.EvoluTCC.controllers;

import com.monografia.EvoluTCC.models.Monografia;
import com.monografia.EvoluTCC.models.Usuario;
import com.monografia.EvoluTCC.repositories.UsuarioRepository;
import com.monografia.EvoluTCC.Enums.StatusMonografia;
import com.monografia.EvoluTCC.dto.MonografiaResponseDTO;
import com.monografia.EvoluTCC.services.MonografiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/monografias")
public class MonografiaController {

    @Autowired
    private MonografiaService monografiaService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Criar uma nova monografia
    @PostMapping("/create/")
public ResponseEntity<Monografia> createMonografia(
        @RequestParam("tema") String tema,
        @RequestParam("especialidadeId") UUID especialidadeId,
        @RequestParam("orientadorId") UUID orientadorId,
        @RequestParam("extratoBancario") MultipartFile extratoBancario,
        @RequestParam("declaracaoNotas") MultipartFile declaracaoNotas,
        @RequestParam("termoOrientador") MultipartFile termoOrientador,
        @RequestParam("projeto") MultipartFile projeto,
        @RequestParam("documentoBi") MultipartFile documentoBi,
        @RequestParam("alunoId") UUID alunoId) throws IOException {

    Monografia monografia = monografiaService.createMonografia(
            tema, extratoBancario, termoOrientador, declaracaoNotas, projeto, documentoBi,
            alunoId, orientadorId, especialidadeId);

    return ResponseEntity.ok(monografia);
}

    // Atualizar monografia com correções
    @PutMapping("/{id}")
    public ResponseEntity<Monografia> updateMonografia(
            @PathVariable UUID id,
            @RequestParam(value = "tema", required = false) String tema,
            @RequestParam(value = "extratoBancario", required = false) MultipartFile extratoBancario,
            @RequestParam(value = "termoOrientador", required = false) MultipartFile termoOrientador,
            @RequestParam(value = "declaracaoNotas", required = false) MultipartFile declaracaoNotas,
            @RequestParam(value = "projeto", required = false) MultipartFile projeto,
            @RequestParam(value = "documentoBi", required = false) MultipartFile documentoBi) throws IOException {

        Monografia monografia = monografiaService.updateMonografia(id, tema, extratoBancario, termoOrientador, declaracaoNotas, projeto, documentoBi);
        return ResponseEntity.ok(monografia);
    }

    @GetMapping("/orientadores/{especialidadeId}")
public ResponseEntity<List<Usuario>> getOrientadoresPorEspecialidade(@PathVariable UUID especialidadeId) {
    List<Usuario> orientadores = usuarioRepository.findByEspecialidadeIdAndTipoUsuarioNome(especialidadeId, "Orientador");
    return ResponseEntity.ok(orientadores);
}

    // Revisão do Orientador
    @PutMapping("/{monografiaId}/revisao-orientador")
    public ResponseEntity<Monografia> reviewMonografia(
            @PathVariable UUID monografiaId,
            @RequestParam("novoStatus") StatusMonografia novoStatus,
            @RequestParam(value = "descricao", required = false) String descricao,
            @RequestParam("orientadorId") UUID orientadorId) {

        Monografia monografia = monografiaService.reviewMonografia(monografiaId, novoStatus, descricao, orientadorId);
        return ResponseEntity.ok(monografia);
    }

    // Revisão do Admin
    @PutMapping("/{monografiaId}/revisao-admin")
    public ResponseEntity<Monografia> adminReviewMonografia(
            @PathVariable UUID monografiaId,
            @RequestParam("novoStatus") StatusMonografia novoStatus,
            @RequestParam(value = "descricao", required = false) String descricao,
            @RequestParam("adminId") UUID adminId) {

        Monografia monografia = monografiaService.adminReviewMonografia(monografiaId, novoStatus, descricao, adminId);
        return ResponseEntity.ok(monografia);
    }

    @GetMapping("/aluno/{alunoId}")
    public ResponseEntity<MonografiaResponseDTO> getMonografiaByAlunoId(@PathVariable UUID alunoId) {
        MonografiaResponseDTO monografiaDTO = monografiaService.getMonografiaByAlunoId(alunoId);
        return ResponseEntity.ok(monografiaDTO);
    }

    @GetMapping("/{id}/documentos/{tipoDocumento}/visualizar")
public ResponseEntity<byte[]> visualizarDocumento(
        @PathVariable UUID id,
        @PathVariable String tipoDocumento) {

    Monografia monografia = monografiaService.getMonografiaById(id);
    byte[] documento = null;
    String nomeArquivo = tipoDocumento + ".pdf";

    switch (tipoDocumento) {
        case "extrato_bancario":
            documento = monografia.getExtratoBancario();
            break;
        case "declaracao_notas":
            documento = monografia.getDeclaracaoNotas();
            break;
        case "termo_orientador":
            documento = monografia.getTermoOrientador();
            break;
        case "projeto":
            documento = monografia.getProjeto();
            break;
        case "documento_bi":
            documento = monografia.getDocumentoBi();
            break;
        default:
            return ResponseEntity.badRequest().build();
    }

    if (documento == null || documento.length == 0) {
        return ResponseEntity.notFound().build();
    }

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_PDF);
    headers.setContentDispositionFormData("inline", nomeArquivo);

    return ResponseEntity.ok()
            .headers(headers)
            .body(documento);
}

    @GetMapping("/{id}/documentos/extrato_bancario/visualizar")
public ResponseEntity<byte[]> visualizarExtratoBancario(@PathVariable UUID id) {
    Monografia monografia = monografiaService.getMonografiaById(id);

    if (monografia.getExtratoBancario() == null || monografia.getExtratoBancario().length == 0) {
        return ResponseEntity.notFound().build(); // Retorna 404 se o arquivo não existir
    }

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_PDF);
    headers.setContentDispositionFormData("inline", "extrato_bancario.pdf"); // "inline" para exibir no navegador

    return ResponseEntity.ok()
            .headers(headers)
            .body(monografia.getExtratoBancario());
}
    // Listar monografias aprovadas para revisão do Admin
    @GetMapping("/aprovadas")
    public ResponseEntity<List<Monografia>> getMonografiasAprovadas() {
        List<Monografia> monografias = monografiaService.getMonografiasAprovadas();
        return ResponseEntity.ok(monografias);
    }

    // Baixar ou visualizar documentos da monografia
    @GetMapping("/{monografiaId}/documento")
    public ResponseEntity<byte[]> getDocumento(
            @PathVariable UUID monografiaId,
            @RequestParam("tipoDocumento") String tipoDocumento,
            @RequestParam(value = "acao", defaultValue = "visualizar") String acao) {

        return monografiaService.getDocumento(monografiaId, tipoDocumento, acao);
    }


    // Listar monografias de um orientador específico
    @GetMapping("/orientador/{orientadorId}")
    public ResponseEntity<List<MonografiaResponseDTO>> getMonografiasPorOrientador(@PathVariable UUID orientadorId) {
        List<MonografiaResponseDTO> monografias = monografiaService.getMonografiasPorOrientador(orientadorId);
        return ResponseEntity.ok(monografias);
    }
    

    // Obter detalhes de uma monografia pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<Monografia> getMonografiaById(@PathVariable UUID id) {
        Monografia monografia = monografiaService.getMonografiaById(id);
        return ResponseEntity.ok(monografia);
    }

    @PostMapping("/{monografiaId}/entregar-projeto")
public ResponseEntity<Monografia> entregarProjetoParaPreDefesa(
        @PathVariable UUID monografiaId,
        @RequestParam("projeto") MultipartFile projeto) throws IOException {

    Monografia monografia = monografiaService.entregarProjetoParaPreDefesa(monografiaId, projeto);
    return ResponseEntity.ok(monografia);
}


@GetMapping("/pre-defesa/pendentes")
public ResponseEntity<List<Monografia>> getMonografiasEmPreDefesa() {
    List<Monografia> monografias = monografiaService.getMonografiasEmPreDefesa();
    return ResponseEntity.ok(monografias);
}

    @GetMapping("/aluno/{alunoId}/estatisticas")
    public ResponseEntity<Map<String, Object>> getEstatisticasAluno(@PathVariable UUID alunoId) {
    Map<String, Object> estatisticas = monografiaService.getEstatisticasAluno(alunoId);
    return ResponseEntity.ok(estatisticas);
   }

   @GetMapping("/aluno/{alunoId}/estatisticas-status")
   public ResponseEntity<Map<String, Integer>> getEstatisticasStatusPorAlunoId(@PathVariable UUID alunoId) {
       Map<String, Integer> estatisticas = monografiaService.getEstatisticasStatusPorAlunoId(alunoId);
       return ResponseEntity.ok(estatisticas);
   }

   @GetMapping("/orientador/{orientadorId}/estatisticas")
    public ResponseEntity<Map<String, Object>> getEstatisticasPorOrientador(@PathVariable UUID orientadorId) {
        Map<String, Object> estatisticas = monografiaService.getEstatisticasPorOrientador(orientadorId);
        return ResponseEntity.ok(estatisticas);
    }

    @GetMapping("/orientador/{orientadorId}/monografias/{monografiaId}")
public ResponseEntity<MonografiaResponseDTO> getMonografiaByOrientadorId(
        @PathVariable UUID orientadorId,
        @PathVariable UUID monografiaId) {

    MonografiaResponseDTO monografia = monografiaService.getMonografiaByOrientadorId(orientadorId, monografiaId);
    return ResponseEntity.ok(monografia);
}
}
