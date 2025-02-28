package com.monografia.EvoluTCC.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.monografia.EvoluTCC.dto.UsuarioDTOResponse;
import com.monografia.EvoluTCC.dto.UsuarioDto;
import com.monografia.EvoluTCC.models.Usuario;
import com.monografia.EvoluTCC.services.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/")
    public ResponseEntity<?> criarUsuario(@Valid @RequestBody UsuarioDto usuarioDto) {
        try {
            Usuario usuarioCriado = usuarioService.criarUsuario(usuarioDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCriado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao criar usuário.");
        }
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> listarTodosUsuarios() {
        try {
            List<Usuario> usuarios = usuarioService.listarTodosUsuarios();
            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{id}/")
public ResponseEntity<UsuarioDTOResponse> listarUsuarioPorId(@PathVariable UUID id) {
    Optional<Usuario> usuario = usuarioService.listarUsuarioPorId(id);
    if (usuario.isPresent()) {
        UsuarioDTOResponse usuarioResponse = new UsuarioDTOResponse(usuario.get());
        return ResponseEntity.ok(usuarioResponse);
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
}

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarUsuario(@PathVariable UUID id) {
        try {
            usuarioService.deletarUsuario(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Usuário deletado com sucesso.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar usuário.");
        }
    }
}
