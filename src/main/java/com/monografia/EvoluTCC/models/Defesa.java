package com.monografia.EvoluTCC.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

import com.monografia.EvoluTCC.Enums.StatusDefesa;

@Entity
@Table(name = "defesa")
public class Defesa {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "monografia_id", nullable = false)
    private Monografia monografia;

    @Column(nullable = false)
    private LocalDateTime dataDefesa;

    @ManyToOne
    @JoinColumn(name = "presidente_id", nullable = false)
    private Usuario presidente;

    @ManyToOne
    @JoinColumn(name = "vogal_id", nullable = false)
    private Usuario vogal;

    private Float nota;
    private String observacoes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusDefesa status;

    // Construtor padrão
    public Defesa() {}

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

    public LocalDateTime getDataDefesa() {
        return dataDefesa;
    }

    public void setDataDefesa(LocalDateTime dataDefesa) {
        this.dataDefesa = dataDefesa;
    }

    public Usuario getPresidente() {
        return presidente;
    }

    public void setPresidente(Usuario presidente) {
        this.presidente = presidente;
    }

    public Usuario getVogal() {
        return vogal;
    }

    public void setVogal(Usuario vogal) {
        this.vogal = vogal;
    }

    public Float getNota() {
        return nota;
    }

    public void setNota(Float nota) {
        this.nota = nota;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public StatusDefesa getStatus() {
        return status;
    }

    public void setStatus(StatusDefesa status) {
        this.status = status;
    }
}
