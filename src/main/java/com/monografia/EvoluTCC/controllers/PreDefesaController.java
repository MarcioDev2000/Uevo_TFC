package com.monografia.EvoluTCC.controllers;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.monografia.EvoluTCC.models.PreDefesa;
import com.monografia.EvoluTCC.services.PreDefesaService;

@RestController
@RequestMapping("/pre-defesa")
public class PreDefesaController {

    @Autowired
    private PreDefesaService preDefesaService;

    @PostMapping("/marcar")
    public ResponseEntity<PreDefesa> marcarPreDefesa(
            @RequestParam UUID monografiaId,
            @RequestParam UUID presidenteId,
            @RequestParam UUID vogalId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataPreDefesa) {

        PreDefesa preDefesa = preDefesaService.marcarPreDefesa(monografiaId, presidenteId, vogalId, dataPreDefesa);
        return ResponseEntity.ok(preDefesa);
    }
}