package com.monografia.EvoluTCC.repositories;

import com.monografia.EvoluTCC.models.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DepartamentoRepository extends JpaRepository<Departamento, UUID> {
    Optional<Departamento> findByNome(String nome);
}
