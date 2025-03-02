package com.monografia.EvoluTCC.repositories;

import com.monografia.EvoluTCC.models.PreDefesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PreDefesaRepository extends JpaRepository<PreDefesa, UUID> {}
