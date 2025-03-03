package com.monografia.EvoluTCC.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.monografia.EvoluTCC.models.Usuario;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository 
public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByResetPasswordToken(String resetPasswordToken);
    boolean existsByEmail(String email);
    List<Usuario> findByEspecialidadeIdAndTipoUsuarioNome(UUID especialidadeId, String tipoUsuarioNome);
    List<Usuario> findByTipoUsuario_Nome(String nome);
}