package com.monografia.EvoluTCC.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

import com.monografia.EvoluTCC.Enums.StatusDefesa;

@Entity
@Table(name = "pre_defesa")
public class PreDefesa {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "monografia_id", nullable = false)
    private Monografia monografia;

    @Lob
    @Column(name = "descricao", nullable = true)
    private String descricao;

    @Column(nullable = false)
    private LocalDateTime dataInicio; // Data e hora de início da pré-defesa

    @Column(nullable = false)
    private LocalDateTime dataFim; // Data e hora de término da pré-defesa

    @ManyToOne
    @JoinColumn(name = "presidente_id", nullable = false)
    private Usuario presidente;

    @ManyToOne
    @JoinColumn(name = "vogal_id", nullable = false)
    private Usuario vogal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusDefesa status;

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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDateTime getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDateTime dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDateTime getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDateTime dataFim) {
        this.dataFim = dataFim;
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

    public StatusDefesa getStatus() {
        return status;
    }

    public void setStatus(StatusDefesa status) {
        this.status = status;
    }
}