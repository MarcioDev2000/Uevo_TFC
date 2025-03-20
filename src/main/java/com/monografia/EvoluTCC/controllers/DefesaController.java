package com.monografia.EvoluTCC.controllers;

import com.monografia.EvoluTCC.dto.DefesaDTO;
import com.monografia.EvoluTCC.models.Defesa;
import com.monografia.EvoluTCC.services.DefesaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
@RestController
@RequestMapping("/defesas")
public class DefesaController {

    @Autowired
    private DefesaService defesaService;

    @PostMapping("/marcar")
    public ResponseEntity<Defesa> marcarDefesa(@RequestParam UUID preDefesaId,
                                               @RequestParam LocalDateTime dataInicio,
                                               @RequestParam LocalDateTime dataFim,
                                               @RequestParam UUID presidenteId,
                                               @RequestParam UUID vogalId) {
        Defesa defesa = defesaService.marcarDefesa(preDefesaId, dataInicio, dataFim, presidenteId, vogalId);
        return ResponseEntity.ok(defesa);
    }

    @GetMapping("/aluno/{alunoId}")
    public ResponseEntity<List<DefesaDTO>> listarDefesasPorAluno(@PathVariable UUID alunoId) {
        List<DefesaDTO> defesas = defesaService.listarDefesasPorAluno(alunoId);
        if (defesas.isEmpty()) {
            return ResponseEntity.noContent().build(); // Retorna 204 (No Content) se não houver defesas
        }
        return ResponseEntity.ok(defesas);
    }

    @GetMapping("/orientador/{orientadorId}")
    public ResponseEntity<List<DefesaDTO>> listarDefesasPorOrientador(@PathVariable UUID orientadorId) {
        List<DefesaDTO> defesas = defesaService.listarDefesasPorOrientador(orientadorId);
        if (defesas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(defesas);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<DefesaDTO>> listarDefesasPorPresidenteOuVogal(@PathVariable UUID usuarioId) {
        List<DefesaDTO> defesas = defesaService.listarDefesasPorPresidenteOuVogal(usuarioId);
        if (defesas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(defesas);
    }

    @PutMapping("/aplicarNota/{defesaId}")
public ResponseEntity<Defesa> aplicarNotaObservacao(@PathVariable UUID defesaId,
                                                    @RequestParam Float nota,
                                                    @RequestParam String observacoes,
                                                    @RequestParam UUID usuarioId) {
    if (nota < 10 || nota > 20) {
        throw new RuntimeException("A nota deve estar entre 10 e 20.");
    }
    Defesa defesa = defesaService.aplicarNotaObservacao(defesaId, nota, observacoes, usuarioId);
    return ResponseEntity.ok(defesa);
}

    @GetMapping("/marcadas")
public ResponseEntity<List<DefesaDTO>> listarDefesasMarcadas() {
    List<DefesaDTO> defesas = defesaService.listarDefesasMarcadas();
    if (defesas.isEmpty()) {
        return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok(defesas);
}


@GetMapping("/marcadas/status/{usuarioId}")
public ResponseEntity<List<DefesaDTO>> listarDefesasMarcadasStatus(@PathVariable UUID usuarioId) {
    List<DefesaDTO> defesas = defesaService.listarDefesasMarcadasStatus(usuarioId);
    return defesas.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(defesas);
}

@GetMapping("/marcadas/status/aprovado/{usuarioId}")
public ResponseEntity<List<DefesaDTO>> listarDefesasMarcadasStatusAprovado(@PathVariable UUID usuarioId) {
    List<DefesaDTO> defesas = defesaService.listarDefesasMarcadasStatusAprovado(usuarioId);
    return defesas.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(defesas);
}

}
