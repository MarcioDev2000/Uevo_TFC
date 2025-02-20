package com.monografia.EvoluTCC.dto;

import java.util.UUID;

public class CursoDTO {
    private UUID id;
    private String nome;
    private String descricao;

    public CursoDTO(UUID id, String nome, String descricao) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
    }

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }
}
