package com.monografia.EvoluTCC.models;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "tipo_usuario")
public class TipoUsuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, length = 50)
    private String nome;

    @Column(nullable = false, length = 100)
    private String rota;

    // Construtor padrão
    public TipoUsuario() {}

    // Construtor com parâmetros
    public TipoUsuario(String nome, String rota) {
        this.nome = nome;
        this.rota = rota;
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

    public String getRota() {
        return rota;
    }

    public void setRota(String rota) {
        this.rota = rota;
    }
}
