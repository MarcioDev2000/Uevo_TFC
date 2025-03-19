package com.monografia.EvoluTCC.repositories;

import com.monografia.EvoluTCC.Enums.StatusDefesa;
import com.monografia.EvoluTCC.models.Defesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface DefesaRepository extends JpaRepository<Defesa, UUID> {
    List<Defesa> findByMonografiaAlunoIdAndStatus(UUID alunoId, StatusDefesa status);

    List<Defesa> findByMonografiaOrientadorIdAndStatus(UUID orientadorId, StatusDefesa status);

    List<Defesa> findByPresidenteIdOrVogalIdAndStatus(UUID presidenteId, UUID vogalId, StatusDefesa status);

     boolean existsByDataInicioBetweenAndMonografiaEspecialidadeId(
        LocalDateTime dataInicio, 
        LocalDateTime dataFim, 
        UUID especialidadeId
    );

    List<Defesa> findByStatus(StatusDefesa status);

}