package com.monografia.EvoluTCC.dto;

import com.monografia.EvoluTCC.models.Usuario;

public class UsuarioDTOResponse {
    private String nome;
    private String sobrenome;
    private String endereco;
    private String telefone;
    private String email;
    private String nif;
    private String especialidade; // String (nome da especialidade ou null)
    private String matricula;
    private String tipoUsuario; // Agora é uma String
    private String curso; // Nome do curso

    public UsuarioDTOResponse(Usuario usuario) {
        this.nome = usuario.getNome();
        this.sobrenome = usuario.getSobrenome();
        this.endereco = usuario.getEndereco();
        this.telefone = usuario.getTelefone();
        this.email = usuario.getEmail();
        this.nif = usuario.getNif();
        this.especialidade = usuario.getEspecialidade() != null ? usuario.getEspecialidade().getNome() : null; // Converte para String
        this.matricula = usuario.getMatricula();
        this.tipoUsuario = usuario.getTipoUsuario().getNome(); // Extrai o nome do TipoUsuario
        this.curso = usuario.getCurso() != null ? usuario.getCurso().getNome() : null; // Extrai o nome do Curso
    }

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }
}