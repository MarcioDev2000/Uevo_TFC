package com.monografia.EvoluTCC.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "monografia_disponivel")
public class MonografiaDisponivel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "monografia_id", nullable = false)
    private Monografia monografia;

    @Column(nullable = false)
    private LocalDateTime dataDisponibilizacao;

    // Construtor padrão
    public MonografiaDisponivel() {}

    // Getters e Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Monografia getMonografia() {
        return monografia;
    }

    public void setMonografia(Monografia monografia) {
        this.monografia = monografia;
    }

    public LocalDateTime getDataDisponibilizacao() {
        return dataDisponibilizacao;
    }

    public void setDataDisponibilizacao(LocalDateTime dataDisponibilizacao) {
        this.dataDisponibilizacao = dataDisponibilizacao;
    }
}
