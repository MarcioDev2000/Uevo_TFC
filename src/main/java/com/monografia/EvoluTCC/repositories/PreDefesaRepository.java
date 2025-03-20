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

        boolean existsByMonografiaId(UUID monografiaId);

    // Verifica se existe uma pré-defesa no mesmo intervalo de tempo e especialidade
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END " +
           "FROM PreDefesa p " +
           "WHERE p.monografia.especialidade.id = :especialidadeId " +
           "AND ((p.dataInicio BETWEEN :dataInicio AND :dataFim) OR (p.dataFim BETWEEN :dataInicio AND :dataFim))")
    boolean existsByDataInicioBetweenAndMonografiaEspecialidadeId(
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim,
            @Param("especialidadeId") UUID especialidadeId);

    // Lista pré-defesas por status
    @Query("SELECT p FROM PreDefesa p WHERE p.status = :status")
    List<PreDefesa> findByStatus(@Param("status") StatusDefesa status);

    // Verifica se existe uma pré-defesa ativa para a mesma monografia
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END " +
           "FROM PreDefesa p " +
           "WHERE p.monografia.id = :monografiaId AND p.status = :status")
    boolean existsByMonografiaIdAndStatus(
            @Param("monografiaId") UUID monografiaId,
            @Param("status") StatusDefesa status);

            @Query("SELECT p FROM PreDefesa p WHERE p.monografia.aluno.id = :alunoId")
            List<PreDefesa> findByMonografiaAlunoId(@Param("alunoId") UUID alunoId);
        
            // Busca pré-defesas onde o orientador é o orientador da monografia
            @Query("SELECT p FROM PreDefesa p WHERE p.monografia.orientador.id = :orientadorId")
            List<PreDefesa> findByMonografiaOrientadorId(@Param("orientadorId") UUID orientadorId);
        
            // Busca pré-defesas onde o usuário é presidente ou vogal
            @Query("SELECT p FROM PreDefesa p WHERE p.presidente.id = :presidenteId OR p.vogal.id = :vogalId")
            List<PreDefesa> findByPresidenteIdOrVogalId(
                    @Param("presidenteId") UUID presidenteId,
                    @Param("vogalId") UUID vogalId);

        
}