package com.monografia.EvoluTCC.repositories;

import com.monografia.EvoluTCC.models.Menus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface MenuRepository extends JpaRepository<Menus, UUID> {}