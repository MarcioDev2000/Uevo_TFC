package com.monografia.EvoluTCC.repositories;

import com.monografia.EvoluTCC.models.Especialidade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EspecialidadeRepository extends JpaRepository<Especialidade, UUID> {
    boolean existsByNome(String nome);
    List<Especialidade> findByCursoId(UUID cursoId);
}
