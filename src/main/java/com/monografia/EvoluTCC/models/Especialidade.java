package com.monografia.EvoluTCC.models;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "especialidade")
public class Especialidade {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String nome;

    @ManyToOne
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;

    // Construtor padrão
    public Especialidade() {}

    // Construtor com parâmetros
    public Especialidade(String nome, Curso curso) {
        this.nome = nome;
        this.curso = curso;
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

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }
}
