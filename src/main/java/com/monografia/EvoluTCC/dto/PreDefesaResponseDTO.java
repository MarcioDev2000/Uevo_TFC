package com.monografia.EvoluTCC.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import com.monografia.EvoluTCC.Enums.StatusDefesa;

public class PreDefesaResponseDTO {
    private UUID id;
    private UUID monografiaId;
    private String temaMonografia;
    private String alunoNomeCompleto; 
    private String especialidadeNome; 
    private String orientadorNomeCompleto; 
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private UUID presidenteId;
    private String presidenteNomeCompleto; // Nome + Sobrenome do presidente
    private UUID vogalId;
    private String vogalNomeCompleto; // Nome + Sobrenome do vogal
    private StatusDefesa status;

    // Getters and Setters
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

    public String getAlunoNomeCompleto() {
        return alunoNomeCompleto;
    }

    public void setAlunoNomeCompleto(String alunoNomeCompleto) {
        this.alunoNomeCompleto = alunoNomeCompleto;
    }

    public String getEspecialidadeNome() {
        return especialidadeNome;
    }

    public void setEspecialidadeNome(String especialidadeNome) {
        this.especialidadeNome = especialidadeNome;
    }

    public String getOrientadorNomeCompleto() {
        return orientadorNomeCompleto;
    }

    public void setOrientadorNomeCompleto(String orientadorNomeCompleto) {
        this.orientadorNomeCompleto = orientadorNomeCompleto;
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

    public String getPresidenteNomeCompleto() {
        return presidenteNomeCompleto;
    }

    public void setPresidenteNomeCompleto(String presidenteNome, String presidenteSobrenome) {
        this.presidenteNomeCompleto = presidenteNome + " " + presidenteSobrenome;
    }

    public UUID getVogalId() {
        return vogalId;
    }

    public void setVogalId(UUID vogalId) {
        this.vogalId = vogalId;
    }

    public String getVogalNomeCompleto() {
        return vogalNomeCompleto;
    }

    public void setVogalNomeCompleto(String vogalNome, String vogalSobrenome) {
        this.vogalNomeCompleto = vogalNome + " " + vogalSobrenome;
    }

    public StatusDefesa getStatus() {
        return status;
    }

    public void setStatus(StatusDefesa status) {
        this.status = status;
    }
}
