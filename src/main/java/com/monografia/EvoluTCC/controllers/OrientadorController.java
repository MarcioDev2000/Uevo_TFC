package com.monografia.EvoluTCC.controllers;
import com.monografia.EvoluTCC.dto.AlunoResponseDTO;
import com.monografia.EvoluTCC.dto.CursoDTO;
import com.monografia.EvoluTCC.dto.OrientadorResponseDTO;
import com.monografia.EvoluTCC.models.Curso;
import com.monografia.EvoluTCC.models.Especialidade;
import com.monografia.EvoluTCC.repositories.CursoRepository;
import com.monografia.EvoluTCC.repositories.EspecialidadeRepository;
import com.monografia.EvoluTCC.repositories.UsuarioRepository;
import com.monografia.EvoluTCC.services.MonografiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

     @Autowired
    private MonografiaService monografiaService;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private EspecialidadeRepository especialidadeRepository;

    public OrientadorController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }


@GetMapping("/cursos/{cursoId}/especialidades")
public ResponseEntity<List<Especialidade>> getEspecialidadesPorCurso(@PathVariable UUID cursoId) {
    List<Especialidade> especialidades = especialidadeRepository.findByCursoId(cursoId);
    return ResponseEntity.ok(especialidades);
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

@GetMapping("/cursos")
public ResponseEntity<List<CursoDTO>> getCursos() {
    List<Curso> cursos = cursoRepository.findAll();
    List<CursoDTO> cursoDTOs = cursos.stream()
            .map(curso -> new CursoDTO(curso.getId(), curso.getNome()))
            .collect(Collectors.toList());
    return ResponseEntity.ok(cursoDTOs);
}



@GetMapping("/{orientadorId}/alunos")
public List<AlunoResponseDTO> getAlunosPorOrientador(@PathVariable UUID orientadorId) {
    return monografiaService.getAlunosPorOrientador(orientadorId);
}

@GetMapping("/{orientadorId}")
public ResponseEntity<OrientadorResponseDTO> getOrientadorPorId(@PathVariable UUID orientadorId) {
    return usuarioRepository.findById(orientadorId)
            .map(usuario -> ResponseEntity.ok(new OrientadorResponseDTO(usuario)))
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
}

    
}