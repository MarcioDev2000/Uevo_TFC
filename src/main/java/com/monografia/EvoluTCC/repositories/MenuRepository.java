package com.monografia.EvoluTCC.repositories;

import com.monografia.EvoluTCC.models.Menus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface MenuRepository extends JpaRepository<Menus, UUID> {
    List<Menus> findByTipoUsuarioId(UUID tipoUsuarioId);
}