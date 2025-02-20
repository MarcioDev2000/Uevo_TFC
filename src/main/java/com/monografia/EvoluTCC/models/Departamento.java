package com.monografia.EvoluTCC.models;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "departamento")
public class Departamento {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "nome", nullable = false, length = 255)
    private String nome;

    @ManyToOne
    @JoinColumn(name = "faculdade_id", nullable = false)
    private Faculdade faculdade;

    public Departamento() {
    }

    public Departamento(String nome, Faculdade faculdade) {
        this.nome = nome;
        this.faculdade = faculdade;
    }

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

    public Faculdade getFaculdade() {
        return faculdade;
    }

    public void setFaculdade(Faculdade faculdade) {
        this.faculdade = faculdade;
    }

}
