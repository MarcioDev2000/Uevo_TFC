package com.monografia.EvoluTCC.controllers;
import com.monografia.EvoluTCC.repositories.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orientadores/")
public class OrientadorController {

    private final UsuarioRepository usuarioRepository;

    public OrientadorController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    
    @GetMapping("/especialidade/{especialidadeId}")
public ResponseEntity<List<Map<String, Object>>> getOrientadoresPorEspecialidade(@PathVariable UUID especialidadeId) {
    // Busca os usuários do tipo "Orientador" associados à especialidade
    List<Map<String, Object>> orientadores = usuarioRepository
            .findByEspecialidadeIdAndTipoUsuarioNome(especialidadeId, "Orientador")
            .stream()
            .map(orientador -> Map.<String, Object>of(
                    "id", orientador.getId(), // Adiciona o UUID do orientador
                    "nomeCompleto", orientador.getNome() + " " + orientador.getSobrenome()
            ))
            .collect(Collectors.toList());

    return ResponseEntity.ok(orientadores);
}

    
    
}