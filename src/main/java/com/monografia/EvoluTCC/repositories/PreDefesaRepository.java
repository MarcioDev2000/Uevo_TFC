package com.monografia.EvoluTCC.repositories;

import com.monografia.EvoluTCC.Enums.StatusDefesa;
import com.monografia.EvoluTCC.models.PreDefesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface PreDefesaRepository extends JpaRepository<PreDefesa, UUID> {
     @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END " +
           "FROM PreDefesa p " +
           "WHERE p.monografia.especialidade.id = :especialidadeId " +
           "AND ((p.dataInicio BETWEEN :dataInicio AND :dataFim) OR (p.dataFim BETWEEN :dataInicio AND :dataFim))")
    boolean existsByDataInicioBetweenAndMonografiaEspecialidadeId(
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim,
            @Param("especialidadeId") UUID especialidadeId);

            @Query("SELECT p FROM PreDefesa p WHERE p.status = :status")
            List<PreDefesa> findByStatus(@Param("status") StatusDefesa status);
}
