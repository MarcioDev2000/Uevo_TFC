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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.monografia.EvoluTCC.Enums.StatusDefesa;
import com.monografia.EvoluTCC.dto.PreDefesaDTO;
import com.monografia.EvoluTCC.dto.PreDefesaResponseDTO;
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
        PreDefesaDTO preDefesaDTO = preDefesaService.criarPreDefesa(monografiaId, presidenteId, vogalId, dataInicio, dataFim, descricao);

        // Retorna a resposta com o DTO e o status HTTP 201 (CREATED)
        return ResponseEntity.status(HttpStatus.CREATED).body(preDefesaDTO);
    }


     @GetMapping("/{id}")
    public ResponseEntity<PreDefesaResponseDTO> buscarPreDefesaPorId(@PathVariable UUID id) {
        PreDefesaResponseDTO preDefesaDTO = preDefesaService.buscarPreDefesaPorId(id);
        return ResponseEntity.ok(preDefesaDTO);
    }

    @GetMapping
public ResponseEntity<List<PreDefesaResponseDTO>> listarPreDefesas(@RequestParam(required = false) StatusDefesa status) {
    List<PreDefesaResponseDTO> preDefesas = preDefesaService.listarPreDefesasPorStatus(status);
    return ResponseEntity.ok(preDefesas);
}

}