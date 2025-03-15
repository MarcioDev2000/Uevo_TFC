package com.monografia.EvoluTCC.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.monografia.EvoluTCC.dto.CursoDTO;
import com.monografia.EvoluTCC.dto.EspecialidadeDTO;
import com.monografia.EvoluTCC.dto.UsuarioDTOResponse;
import com.monografia.EvoluTCC.dto.UsuarioDto;
import com.monografia.EvoluTCC.dto.userAllDTO;
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

    @GetMapping("/cursos")
public ResponseEntity<List<CursoDTO>> listarTodosCursos() {
    try {
        List<CursoDTO> cursos = usuarioService.listarTodosCursos();
        return ResponseEntity.ok(cursos);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}

@GetMapping("/cursos/{cursoId}/especialidades")
public ResponseEntity<List<EspecialidadeDTO>> listarEspecialidadesPorCurso(@PathVariable UUID cursoId) {
    try {
        List<EspecialidadeDTO> especialidades = usuarioService.listarEspecialidadesPorCurso(cursoId);
        return ResponseEntity.ok(especialidades);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}

    @GetMapping("/inativos")
public ResponseEntity<List<userAllDTO>> listarUsuariosInativos(@RequestParam UUID adminId) {
    try {
        List<userAllDTO> usuariosInativos = usuarioService.listarUsuariosInativos(adminId);
        return ResponseEntity.ok(usuariosInativos);
    } catch (RuntimeException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}

@PatchMapping("/{id}/status") // Usando PATCH para atualizar apenas o status
public ResponseEntity<?> atualizarStatusUsuario(@PathVariable UUID id, @RequestParam boolean status) {
    try {
        // Chama o método do Service para atualizar o status
        usuarioService.atualizarStatusUsuario(id, status);

        return ResponseEntity.ok(Map.of("message", "Status da conta atualizado com sucesso."));
    } catch (RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
    }
}


@GetMapping
public ResponseEntity<List<UsuarioDTOResponse>> listarTodosUsuarios() {
    try {
        List<UsuarioDTOResponse> usuarios = usuarioService.listarTodosUsuarios();
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

    @GetMapping("/alunos")
    public ResponseEntity<List<UsuarioDTOResponse>> listarTodosAlunos(@RequestParam UUID adminId) {
        try {
            List<UsuarioDTOResponse> alunos = usuarioService.listarTodosAlunos(adminId);
            return ResponseEntity.ok(alunos);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/orientadores")
public ResponseEntity<List<UsuarioDTOResponse>> listarTodosOrientadores(@RequestParam UUID adminId) {
    try {
        List<UsuarioDTOResponse> orientadores = usuarioService.listarTodosOrientadores(adminId);
        return ResponseEntity.ok(orientadores);
    } catch (RuntimeException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}

    
}
