package com.monografia.EvoluTCC.models;

import jakarta.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 100)
    private String sobrenome;

    @Column(columnDefinition = "TEXT")
    private String endereco;

    @Column(length = 20)
    private String telefone;

    @Column(nullable = false, length = 100, unique = true)
    private String email;

    @Column(length = 20, unique = true)
    private String nif;

    @ManyToOne
    @JoinColumn(name = "especialidade_id",  nullable = true)
    private Especialidade especialidade;

    @Column(length = 50)
    private String matricula;

    @ManyToOne
    @JoinColumn(name = "tipo_usuario_id", nullable = false)
    private TipoUsuario tipoUsuario;

    @Column(length = 255)
    private String resetPasswordToken;

    @Temporal(TemporalType.TIMESTAMP)
    private Date tokenExpirationDate;

    // Construtor Padrão
    public Usuario() {}

    // Construtor com parâmetros
    public Usuario(String password, String nome, String sobrenome, String endereco, String telefone,
                   String email, String nif, Especialidade especialidade, String matricula, TipoUsuario tipoUsuario) {
        this.password = password;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.endereco = endereco;
        this.telefone = telefone;
        this.email = email;
        this.nif = nif;
        this.especialidade = especialidade;
        this.matricula = matricula;
        this.tipoUsuario = tipoUsuario;
    }


    // Getters e Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

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

    public Especialidade getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(Especialidade especialidade) {
        this.especialidade = especialidade;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getResetPasswordToken() {
        return resetPasswordToken;
    }

    public void setResetPasswordToken(String resetPasswordToken) {
        this.resetPasswordToken = resetPasswordToken;
    }

    public Date getTokenExpirationDate() {
        return tokenExpirationDate;
    }

    public void setTokenExpirationDate(Date tokenExpirationDate) {
        this.tokenExpirationDate = tokenExpirationDate;
    }
}
