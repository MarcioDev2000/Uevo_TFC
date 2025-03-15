package com.monografia.EvoluTCC.dto;

import java.util.UUID;

public class EspecialidadeDTO {
    private UUID id;
    private String nome;

    // Construtor
    public EspecialidadeDTO(UUID id, String nome) {
        this.id = id;
        this.nome = nome;
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
}