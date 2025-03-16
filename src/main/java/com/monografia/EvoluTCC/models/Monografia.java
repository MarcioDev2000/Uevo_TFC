package com.monografia.EvoluTCC.models;
import java.time.LocalDateTime;
import java.util.UUID;

import com.monografia.EvoluTCC.Enums.StatusMonografia;
import com.monografia.EvoluTCC.Enums.TipoUsuario;

import jakarta.persistence.*;

@Entity
@Table(name = "monografia")
public class Monografia {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "aluno_id", nullable = false)
    private Usuario aluno;

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = true) 
    private Usuario admin;

    @ManyToOne
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;

    @ManyToOne
    @JoinColumn(name = "orientador_id", nullable = false)
    private Usuario orientador;

    @ManyToOne
    @JoinColumn(name = "especialidade_id", nullable = false)
    private Especialidade especialidade;

    @Column(nullable = false, length = 255)
    private String tema;

    @Lob
    @Column(name = "extrato_bancario", nullable = true)
    private byte[] extratoBancario;

    @Lob
    @Column(name = "declaracao_notas", nullable = true)
    private byte[] declaracaoNotas;

    @Lob
    @Column(name = "termo_orientador", nullable = true)
    private byte[] termoOrientador;

    @Lob
    @Column(name = "projeto", nullable = true)
    private byte[] projeto;

    @Lob
    @Column(name = "documento_bi", nullable = true)
    private byte[] documentoBi;

    @Lob
    @Column(name = "termo_do_aluno", nullable = true)
    private byte[] termoDoAluno;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusMonografia status;

    private String descricaoMelhoria;

    @Column(name = "data_status")
    private LocalDateTime dataStatus;

    @Transient 
    private String linkExtratoBancario;

    @Transient
    private String linkDeclaracaoNotas;

    @Transient
    private String linkTermoOrientador;

    @Transient
    private String linkProjeto;

     @Transient
     private String linkDocumentoBi;

     @Transient
     private String linkTermoDoAluno;

     @Enumerated(EnumType.STRING)
     private TipoUsuario aprovadoPor;

    // Construtor padrão
    public Monografia() {}

    public Monografia(Usuario aluno, Usuario admin, Usuario orientador, Especialidade especialidade, String tema,
    byte[] extratoBancario, byte[] declaracaoNotas, byte[] termoOrientador, byte[] projeto,
    byte[] documentoBi, byte[] termoDoAluno, StatusMonografia status, String descricaoMelhoria) {
this.aluno = aluno;
this.admin = admin;
this.orientador = orientador;
this.especialidade = especialidade;
this.tema = tema;
this.extratoBancario = extratoBancario;
this.declaracaoNotas = declaracaoNotas;
this.termoOrientador = termoOrientador;
this.projeto = projeto;
this.documentoBi = documentoBi;
this.termoDoAluno = termoDoAluno;
this.status = status;
this.descricaoMelhoria = descricaoMelhoria;
this.dataStatus = LocalDateTime.now();
}


    // Getters e Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Usuario getAluno() {
        return aluno;
    }

    public void setAluno(Usuario aluno) {
        this.aluno = aluno;
    }

    public Usuario getOrientador() {
        return orientador;
    }

    public void setOrientador(Usuario orientador) {
        this.orientador = orientador;
    }

    public Especialidade getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(Especialidade especialidade) {
        this.especialidade = especialidade;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public byte[] getExtratoBancario() {
        return extratoBancario;
    }

    public void setExtratoBancario(byte[] extratoBancario) {
        this.extratoBancario = extratoBancario;
    }

    public byte[] getDeclaracaoNotas() {
        return declaracaoNotas;
    }

    public void setDeclaracaoNotas(byte[] declaracaoNotas) {
        this.declaracaoNotas = declaracaoNotas;
    }

    public byte[] getTermoOrientador() {
        return termoOrientador;
    }

    public void setTermoOrientador(byte[] termoOrientador) {
        this.termoOrientador = termoOrientador;
    }

    public byte[] getProjeto() {
        return projeto;
    }

    public void setProjeto(byte[] projeto) {
        this.projeto = projeto;
    }

    public byte[] getDocumentoBi() {
        return documentoBi;
    }

    public void setDocumentoBi(byte[] documentoBi) {
        this.documentoBi = documentoBi;
    }

    public StatusMonografia getStatus() {
        return status;
    }

    public void setStatus(StatusMonografia status) {
        this.status = status;
    }

    public String getDescricaoMelhoria() {
        return descricaoMelhoria;
    }

    public void setDescricaoMelhoria(String descricaoMelhoria) {
        this.descricaoMelhoria = descricaoMelhoria;
    }

    public Usuario getAdmin() {
        return admin;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }
    
    public void setAdmin(Usuario admin) {
        this.admin = admin;
    }

    public LocalDateTime getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(LocalDateTime dataStatus) {
        this.dataStatus = dataStatus;
    }

    public String getLinkExtratoBancario() {
        return linkExtratoBancario;
    }
    
    public void setLinkExtratoBancario(String linkExtratoBancario) {
        this.linkExtratoBancario = linkExtratoBancario;
    }
    
    public String getLinkDeclaracaoNotas() {
        return linkDeclaracaoNotas;
    }
    
    public void setLinkDeclaracaoNotas(String linkDeclaracaoNotas) {
        this.linkDeclaracaoNotas = linkDeclaracaoNotas;
    }
    
    public String getLinkTermoOrientador() {
        return linkTermoOrientador;
    }
    
    public void setLinkTermoOrientador(String linkTermoOrientador) {
        this.linkTermoOrientador = linkTermoOrientador;
    }
    
    public String getLinkProjeto() {
        return linkProjeto;
    }
    
    public void setLinkProjeto(String linkProjeto) {
        this.linkProjeto = linkProjeto;
    }
    
    public String getLinkDocumentoBi() {
        return linkDocumentoBi;
    }
    
    public void setLinkDocumentoBi(String linkDocumentoBi) {
        this.linkDocumentoBi = linkDocumentoBi;
    }

    public byte[] getTermoDoAluno() {
        return termoDoAluno;
    }

    public void setTermoDoAluno(byte[] termoDoAluno) {
        this.termoDoAluno = termoDoAluno;
    }

    public String getLinkTermoDoAluno() {
        return linkTermoDoAluno;
    }

    public void setLinkTermoDoAluno(String linkTermoDoAluno) {
        this.linkTermoDoAluno = linkTermoDoAluno;
    }

    public TipoUsuario getAprovadoPor() {
        return aprovadoPor;
    }
    
    public void setAprovadoPor(TipoUsuario aprovadoPor) {
        this.aprovadoPor = aprovadoPor;
    }
    

}
