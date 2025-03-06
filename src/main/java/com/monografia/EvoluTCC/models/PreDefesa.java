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
    @Column(name = "projeto", columnDefinition = "BYTEA", nullable = true)
    private byte[] projeto;


    @Lob
    @Column(name = "descricao", nullable = true)
    private String descricao;


    @Column(nullable = false)
    private LocalDateTime dataPreDefesa;

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

    public byte[] getProjeto() {
        return projeto;
    }

    public void setProjeto(byte[] projeto) {
        this.projeto = projeto;
    }

    public LocalDateTime getDataPreDefesa() {
        return dataPreDefesa;
    }

    public void setDataPreDefesa(LocalDateTime dataPreDefesa) {
        this.dataPreDefesa = dataPreDefesa;
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
