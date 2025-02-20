package com.monografia.EvoluTCC.controllers;

import com.monografia.EvoluTCC.models.Especialidade;
import com.monografia.EvoluTCC.repositories.EspecialidadeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@RestController
@RequestMapping("/especialidades")
public class EspecialidadeController {

    private final EspecialidadeRepository especialidadeRepository;

    public EspecialidadeController(EspecialidadeRepository especialidadeRepository) {
        this.especialidadeRepository = especialidadeRepository;
    }

    // Endpoint para listar todas as especialidades
    @GetMapping
    public List<Especialidade> getAllEspecialidades() {
        return especialidadeRepository.findAll();
    }

    // Endpoint para buscar uma especialidade pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<Especialidade> getEspecialidadeById(@PathVariable UUID id) {
        Optional<Especialidade> especialidade = especialidadeRepository.findById(id);
        return especialidade.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint para criar uma nova especialidade
    @PostMapping
    public ResponseEntity<Especialidade> createEspecialidade(@RequestBody Especialidade especialidade) {
        Especialidade savedEspecialidade = especialidadeRepository.save(especialidade);
        return ResponseEntity.status(201).body(savedEspecialidade);
    }

    // Endpoint para atualizar uma especialidade existente
    @PutMapping("/{id}")
    public ResponseEntity<Especialidade> updateEspecialidade(
            @PathVariable UUID id, @RequestBody Especialidade especialidadeDetails) {
        return especialidadeRepository.findById(id)
                .map(especialidade -> {
                    especialidade.setNome(especialidadeDetails.getNome());
                    Especialidade updatedEspecialidade = especialidadeRepository.save(especialidade);
                    return ResponseEntity.ok(updatedEspecialidade);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

  
}
