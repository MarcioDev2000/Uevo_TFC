package com.monografia.EvoluTCC.controllers;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.monografia.EvoluTCC.Enums.StatusDefesa;
import com.monografia.EvoluTCC.dto.PreDefesaDTO;
import com.monografia.EvoluTCC.dto.PreDefesaResponseDTO;
import com.monografia.EvoluTCC.models.Monografia;
import com.monografia.EvoluTCC.services.PreDefesaService;

@RestController
@RequestMapping("/pre-defesas")
public class PreDefesaController {

    @Autowired
    private PreDefesaService preDefesaService;

     @PostMapping
    public ResponseEntity<PreDefesaDTO> criarPreDefesa(
            @RequestParam UUID monografiaId,
            @RequestParam UUID presidenteId,
            @RequestParam UUID vogalId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim,
            @RequestParam(required = false) String descricao) {

        // Chama o serviço para criar a pré-defesa
        PreDefesaDTO preDefesaDTO = preDefesaService.criarPreDefesa(monografiaId, presidenteId, vogalId, dataInicio, dataFim);

        // Retorna a resposta com o DTO e o status HTTP 201 (CREATED)
        return ResponseEntity.status(HttpStatus.CREATED).body(preDefesaDTO);
    }

    @GetMapping("/preDefesaStatusmonografia/{usuarioId}")
public ResponseEntity<List<PreDefesaResponseDTO>> listarPreDefesasComStatusMonografia(@PathVariable UUID usuarioId) {
    // Chama o serviço para listar as pré-defesas com status da monografia EM_PRE_DEFESA
    List<PreDefesaResponseDTO> preDefesas = preDefesaService.listarPreDefesasComStatusPreMonografia(usuarioId);

    // Retorna a resposta com a lista de DTOs e o status HTTP 200 (OK)
    return ResponseEntity.ok(preDefesas);
}

     @GetMapping("/{id}")
    public ResponseEntity<PreDefesaResponseDTO> buscarPreDefesaPorId(@PathVariable UUID id) {
        PreDefesaResponseDTO preDefesaDTO = preDefesaService.buscarPreDefesaPorId(id);
        return ResponseEntity.ok(preDefesaDTO);
    }

    @GetMapping("/{preDefesaId}/detalhes")
    public ResponseEntity<PreDefesaResponseDTO> detalharPreDefesaPorIdEUsuario(
            @PathVariable UUID preDefesaId,
            @RequestParam UUID usuarioId) {
        try {
            // Chama o serviço para obter os detalhes da pré-defesa
            PreDefesaResponseDTO detalhesPreDefesa = preDefesaService.detalharPreDefesaPorIdEUsuario(preDefesaId, usuarioId);

            // Retorna a resposta com o DTO e o status HTTP 200 (OK)
            return ResponseEntity.ok(detalhesPreDefesa);
        } catch (RuntimeException e) {
            // Retorna uma resposta de erro com o status HTTP 403 (FORBIDDEN) se o usuário não tiver permissão
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }


    @GetMapping
public ResponseEntity<List<PreDefesaResponseDTO>> listarPreDefesas(@RequestParam(required = false) StatusDefesa status) {
    List<PreDefesaResponseDTO> preDefesas = preDefesaService.listarPreDefesasPorStatus(status);
    return ResponseEntity.ok(preDefesas);
}

@GetMapping("/usuario/{usuarioId}")
public ResponseEntity<List<PreDefesaResponseDTO>> listarPreDefesasPorUsuario(@PathVariable UUID usuarioId) {
    // Chama o serviço para listar as pré-defesas associadas ao usuário
    List<PreDefesaResponseDTO> preDefesas = preDefesaService.listarPreDefesasDoUsuario(usuarioId);

    // Retorna a resposta com a lista de DTOs e o status HTTP 200 (OK)
    return ResponseEntity.ok(preDefesas);
}

@GetMapping("/admin")
public ResponseEntity<List<PreDefesaResponseDTO>> listarTodasPreDefesas() {
    // Chama o serviço para listar todas as pré-defesas
    List<PreDefesaResponseDTO> preDefesas = preDefesaService.listarTodasPreDefesas();

    // Retorna a resposta com a lista de DTOs e o status HTTP 200 (OK)
    return ResponseEntity.ok(preDefesas);
}

@PutMapping("/{preDefesaId}/status")
    public PreDefesaResponseDTO atualizarStatusPreDefesa(
            @PathVariable UUID preDefesaId,
            @RequestParam StatusDefesa status,
            @RequestParam UUID usuarioId,
            @RequestParam(required = false) String descricao) {
        return preDefesaService.atualizarStatusPreDefesa(preDefesaId, status, usuarioId, descricao);
    }

  @GetMapping("/monografias-em-pre-defesa")
    public List<Monografia> listarMonografiasEmPreDefesa() {
        return preDefesaService.listarMonografiasEmPreDefesa();
    }

    @GetMapping("/usuario/{usuarioId}/filtradas")
    public ResponseEntity<List<PreDefesaResponseDTO>> listarPreDefesasDoUsuarioFiltradas(@PathVariable UUID usuarioId) {
        // Chama o serviço para listar as pré-defesas filtradas
        List<PreDefesaResponseDTO> preDefesas = preDefesaService.listarPreDefesasDoUsuarioFiltradas(usuarioId);

        // Retorna a resposta com a lista de DTOs e o status HTTP 200 (OK)
        return ResponseEntity.ok(preDefesas);
    }

    @GetMapping("/{preDefesaId}/visualizar")
    public PreDefesaResponseDTO visualizarPreDefesa(
            @PathVariable UUID preDefesaId,
            @RequestParam UUID usuarioId) {
        return preDefesaService.visualizarPreDefesa(preDefesaId, usuarioId);
    }

}