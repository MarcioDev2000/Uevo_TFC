package com.monografia.EvoluTCC.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.monografia.EvoluTCC.Enums.StatusDefesa;

public class PreDefesaDTO {

    private UUID id;
    private UUID monografiaId;
    private String temaMonografia; 
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim; 
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

    public String getTemaMonografia() {
        return temaMonografia;
    }

    public void setTemaMonografia(String temaMonografia) {
        this.temaMonografia = temaMonografia;
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