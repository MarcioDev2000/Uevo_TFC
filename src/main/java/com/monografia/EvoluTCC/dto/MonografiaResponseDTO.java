package com.monografia.EvoluTCC.dto;

import java.util.UUID;

public class MonografiaResponseDTO {
    private UUID id;
    private String tema;
    private String status;
    private String linkExtratoBancario;
    private String linkDeclaracaoNotas;
    private String linkTermoOrientador;
    private String linkProjeto;
    private String linkDocumentoBi;

    // Getters e Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLinkExtratoBancario() {
        return linkExtratoBancario;
    }

    public void setLinkExtratoBancario(String linkExtratoBancario) {
        this.linkExtratoBancario = linkExtratoBancario;
    }

    public String getLinkDeclaracaoNotas() {
        return linkDeclaracaoNotas;
    }

    public void setLinkDeclaracaoNotas(String linkDeclaracaoNotas) {
        this.linkDeclaracaoNotas = linkDeclaracaoNotas;
    }

    public String getLinkTermoOrientador() {
        return linkTermoOrientador;
    }

    public void setLinkTermoOrientador(String linkTermoOrientador) {
        this.linkTermoOrientador = linkTermoOrientador;
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
}