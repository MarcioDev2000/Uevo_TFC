package com.monografia.EvoluTCC.models;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "menus")
public class Menus {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String icon;

    @Column(nullable = false)
    private String rota;

    @Column(nullable = false)
    private String id_do_usuario;

    // Construtor padrão
    public Menus() {}

    // Construtor com parâmetros (Este é o que deve ser chamado no MenuSeeder)
    public Menus(String nome, String icon, String rota, String id_do_usuario) {
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

    public String getIdDoUsuario() {
        return id_do_usuario;
    }

    public void setIdDoUsuario(String id_do_usuario) {
        this.id_do_usuario = id_do_usuario;
    }
}
