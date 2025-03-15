package com.monografia.EvoluTCC.dto;

import java.util.UUID;

import com.monografia.EvoluTCC.models.Usuario;

public class userAllDTO {
    private UUID id;
    private String nome;
    private String sobrenome;
    private String endereco;
    private String telefone;
    private String email;
    private String nif;
    private String especialidade;
    private String matricula;
    private String tipoUsuario;
    private String status; // Agora o status é String ("Ativo" ou "Inativo")

    public userAllDTO(Usuario usuario) {
        this.id = usuario.getId(); 
        this.nome = usuario.getNome();
        this.sobrenome = usuario.getSobrenome();
        this.endereco = usuario.getEndereco();
        this.telefone = usuario.getTelefone();
        this.email = usuario.getEmail();
        this.nif = usuario.getNif();
        this.especialidade = usuario.getEspecialidade() != null ? usuario.getEspecialidade().getNome() : null;
        this.matricula = usuario.getMatricula();
        this.tipoUsuario = usuario.getTipoUsuario().getNome();
        this.status = usuario.isStatus() ? "Ativo" : "Inativo"; // Converte boolean para texto
    }

    public UUID getId() { return id; } // Getter do ID
    public void setId(UUID id) { this.id = id; } // Setter do ID

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getSobrenome() { return sobrenome; }
    public void setSobrenome(String sobrenome) { this.sobrenome = sobrenome; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNif() { return nif; }
    public void setNif(String nif) { this.nif = nif; }

    public String getEspecialidade() { return especialidade; }
    public void setEspecialidade(String especialidade) { this.especialidade = especialidade; }

    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }

    public String getTipoUsuario() { return tipoUsuario; }
    public void setTipoUsuario(String tipoUsuario) { this.tipoUsuario = tipoUsuario; }

    public String getStatus() { return status; } // Retorna "Ativo" ou "Inativo"
    public void setStatus(boolean status) { this.status = status ? "Ativo" : "Inativo"; }
}
