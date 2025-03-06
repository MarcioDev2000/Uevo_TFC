package com.monografia.EvoluTCC.repositories;

import com.monografia.EvoluTCC.models.Monografia;
import com.monografia.EvoluTCC.Enums.StatusMonografia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MonografiaRepository extends JpaRepository<Monografia, UUID> {

    // Buscar monografias por status
    List<Monografia> findByStatus(StatusMonografia status);

    // Buscar monografias por aluno
    List<Monografia> findByAlunoId(UUID alunoId);

    // Buscar monografias por orientador
    List<Monografia> findByOrientadorId(UUID orientadorId);

    // Buscar monografias por especialidade
    List<Monografia> findByEspecialidadeId(UUID especialidadeId);

    // Buscar monografias por aluno e status
    List<Monografia> findByAlunoIdAndStatus(UUID alunoId, StatusMonografia status);

    // Buscar monografias por orientador e status
    List<Monografia> findByOrientadorIdAndStatus(UUID orientadorId, StatusMonografia status);

    // Buscar monografias por tema (usando LIKE para busca parcial)
    List<Monografia> findByTemaContainingIgnoreCase(String tema);

    // Buscar monografias por status e tema
    List<Monografia> findByStatusAndTemaContainingIgnoreCase(StatusMonografia status, String tema);

    // Buscar monografia por aluno e carregar orientador
    @Query("SELECT m FROM Monografia m JOIN FETCH m.orientador WHERE m.aluno.id = :alunoId")
    Optional<Monografia> findByAlunoIdWithOrientador(@Param("alunoId") UUID alunoId);

    // Verificar se existe monografia para um aluno (garantindo que só há uma definição)
    boolean existsByAlunoId(UUID alunoId); 

    boolean existsByAlunoIdAndOrientadorIdNot(UUID alunoId, UUID orientadorId);

    long countByStatus(StatusMonografia status);

    long countByOrientadorId(UUID orientadorId);
}
