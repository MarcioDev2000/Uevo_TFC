package com.monografia.EvoluTCC.repositories;

import com.monografia.EvoluTCC.models.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CursoRepository extends JpaRepository<Curso, UUID> {
     Optional<Curso> findByNome(String nome);
}
