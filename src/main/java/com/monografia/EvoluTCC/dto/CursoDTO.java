package com.monografia.EvoluTCC.dto;

import java.util.UUID;

public class CursoDTO {
    private UUID id;
    private String nome;


    public CursoDTO(UUID id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }
}
