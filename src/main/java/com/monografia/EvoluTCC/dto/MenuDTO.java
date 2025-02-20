package com.monografia.EvoluTCC.dto;

import java.util.UUID;

public class MenuDTO {
    private UUID id;
    private String nome;
    private String icon;
    private String rota;
    private String id_do_usuario;

    // Construtor padrão
    public MenuDTO() {}

    // Construtor com parâmetros
    public MenuDTO(UUID id, String nome, String icon, String rota, String id_do_usuario) {
        this.id = id;
        this.nome = nome;
        this.icon = icon;
        this.rota = rota;
        this.id_do_usuario = id_do_usuario;
    }

    // Getters e Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getRota() {
        return rota;
    }

    public void setRota(String rota) {
        this.rota = rota;
    }

    public String getId_do_usuario() {
        return id_do_usuario;
    }

    public void setId_do_usuario(String id_do_usuario) {
        this.id_do_usuario = id_do_usuario;
    }
}