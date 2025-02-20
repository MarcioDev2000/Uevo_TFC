package com.monografia.EvoluTCC.repositories;

import com.monografia.EvoluTCC.models.Faculdade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FaculdadeRepository extends JpaRepository<Faculdade, UUID> {
    Optional<Faculdade> findByNome(String nome);
}
