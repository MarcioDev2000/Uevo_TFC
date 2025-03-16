package com.monografia.EvoluTCC.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.monografia.EvoluTCC.Enums.StatusDefesa;
import com.monografia.EvoluTCC.Enums.StatusMonografia;

public class PreDefesaResponseDTO {
    private UUID id;
    private UUID monografiaId;
    private String temaMonografia;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private String descricao;
    private UUID presidenteId;
    private String presidenteNomeCompleto;
    private UUID vogalId;
    private String vogalNomeCompleto;
    private String alunoNomeCompleto;
    private String especialidadeNome;
    private String orientadorNomeCompleto;
    private StatusMonografia statusMonografia;
    private StatusDefesa status;

    // Links para os documentos da monografia
    private String linkExtratoBancario;
    private String linkTermoOrientador;
    private String linkDeclaracaoNotas;
    private String linkProjeto;
    private String linkDocumentoBi;
    private String linkTermoDoAluno;

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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
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

    public void setPresidenteNomeCompleto(String presidenteNomeCompleto) {
        this.presidenteNomeCompleto = presidenteNomeCompleto;
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

    public void setVogalNomeCompleto(String vogalNomeCompleto) {
        this.vogalNomeCompleto = vogalNomeCompleto;
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

    public StatusMonografia getStatusMonografia() {
        return statusMonografia;
    }

    public void setStatusMonografia(StatusMonografia statusMonografia) {
        this.statusMonografia = statusMonografia;
    }

    public String getLinkExtratoBancario() {
        return linkExtratoBancario;
    }

    public void setLinkExtratoBancario(String linkExtratoBancario) {
        this.linkExtratoBancario = linkExtratoBancario;
    }

    public String getLinkTermoOrientador() {
        return linkTermoOrientador;
    }

    public void setLinkTermoOrientador(String linkTermoOrientador) {
        this.linkTermoOrientador = linkTermoOrientador;
    }

    public String getLinkDeclaracaoNotas() {
        return linkDeclaracaoNotas;
    }

    public void setLinkDeclaracaoNotas(String linkDeclaracaoNotas) {
        this.linkDeclaracaoNotas = linkDeclaracaoNotas;
    }

    public String getLinkProjeto() {
        return linkProjeto;
    }

    public void setLinkProjeto(String linkProjeto) {
        this.linkProjeto = linkProjeto;
    }

    public String getLinkDocumentoBi() {
        return linkDocumentoBi;
    }

    public void setLinkDocumentoBi(String linkDocumentoBi) {
        this.linkDocumentoBi = linkDocumentoBi;
    }

    public String getLinkTermoDoAluno() {
        return linkTermoDoAluno;
    }

    public void setLinkTermoDoAluno(String linkTermoDoAluno) {
        this.linkTermoDoAluno = linkTermoDoAluno;
    }

    public StatusDefesa getStatus() {
        return status;
    }

    public void setStatus(StatusDefesa status) {
        this.status = status;
    }
}