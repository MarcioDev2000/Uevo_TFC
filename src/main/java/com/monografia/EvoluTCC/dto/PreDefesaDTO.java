package com.monografia.EvoluTCC.dto;
import java.time.LocalDateTime;
import java.util.UUID;

import com.monografia.EvoluTCC.Enums.StatusDefesa;

public class PreDefesaDTO {
    
    private UUID id;
    private UUID monografiaId;
    private String projeto;
    private String descricao;
    private LocalDateTime dataPreDefesa;
    private UUID presidenteId;
    private UUID vogalId;
    private StatusDefesa status;

    // Getters e Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getMonografiaId() {
        return monografiaId;
    }

    public void setMonografiaId(UUID monografiaId) {
        this.monografiaId = monografiaId;
    }

    public String getProjeto() {
        return projeto;
    }

    public void setProjeto(String projeto) {
        this.projeto = projeto;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDateTime getDataPreDefesa() {
        return dataPreDefesa;
    }

    public void setDataPreDefesa(LocalDateTime dataPreDefesa) {
        this.dataPreDefesa = dataPreDefesa;
    }

    public UUID getPresidenteId() {
        return presidenteId;
    }

    public void setPresidenteId(UUID presidenteId) {
        this.presidenteId = presidenteId;
    }

    public UUID getVogalId() {
        return vogalId;
    }

    public void setVogalId(UUID vogalId) {
        this.vogalId = vogalId;
    }

    public StatusDefesa getStatus() {
        return status;
    }

    public void setStatus(StatusDefesa status) {
        this.status = status;
    }
}
