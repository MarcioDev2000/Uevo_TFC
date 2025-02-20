package com.monografia.EvoluTCC.controllers;

import com.monografia.EvoluTCC.models.Monografia;
import com.monografia.EvoluTCC.Enums.StatusMonografia;
import com.monografia.EvoluTCC.services.MonografiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/monografias")
public class MonografiaController {

    @Autowired
    private MonografiaService monografiaService;

    // Criar uma nova monografia
    @PostMapping
    public ResponseEntity<Monografia> createMonografia(
            @RequestParam("tema") String tema,
            @RequestParam("extratoBancario") MultipartFile extratoBancario,
            @RequestParam("termoOrientador") MultipartFile termoOrientador,
            @RequestParam("declaracaoNotas") MultipartFile declaracaoNotas,
            @RequestParam("projeto") MultipartFile projeto,
            @RequestParam("documentoBi") MultipartFile documentoBi,
            @RequestParam("alunoId") UUID alunoId,
            @RequestParam("orientadorId") UUID orientadorId,
            @RequestParam("especialidadeId") UUID especialidadeId) throws IOException {

        Monografia monografia = monografiaService.createMonografia(tema, extratoBancario, termoOrientador,
                declaracaoNotas, projeto, documentoBi, alunoId, orientadorId, especialidadeId);
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
    public ResponseEntity<List<Monografia>> getMonografiasPorOrientador(@PathVariable UUID orientadorId) {
        List<Monografia> monografias = monografiaService.getMonografiasPorOrientador(orientadorId);
        return ResponseEntity.ok(monografias);
    }

    // Obter detalhes de uma monografia pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<Monografia> getMonografiaById(@PathVariable UUID id) {
        Monografia monografia = monografiaService.getMonografiaById(id);
        return ResponseEntity.ok(monografia);
    }
}
