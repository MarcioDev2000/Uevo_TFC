package com.monografia.EvoluTCC.repositories;

import com.monografia.EvoluTCC.models.TipoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TipoUsuarioRepository extends JpaRepository<TipoUsuario, UUID> {
    Optional<TipoUsuario> findByNome(String nome);
    boolean existsByNome(String nome);
}
