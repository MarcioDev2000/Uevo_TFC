package com.monografia.EvoluTCC.producers;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.monografia.EvoluTCC.dto.EmailDto;
import com.monografia.EvoluTCC.models.Defesa;
import com.monografia.EvoluTCC.models.Monografia;
import com.monografia.EvoluTCC.models.PreDefesa;
import com.monografia.EvoluTCC.models.Usuario;
import com.monografia.EvoluTCC.repositories.UsuarioRepository;

@Component
public class UserProducer {

   
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Value("${spring.rabbitmq.queue}")
    private String queue;

    

    public void sendEmail(Usuario usuario, String subject, String text) {
        EmailDto emailDto = new EmailDto();
        emailDto.setUserId(usuario.getId());
        emailDto.setEmailFrom("pesselaabreu79@gmail.com"); // Defina o email de origem
        emailDto.setEmailTo(usuario.getEmail());
        emailDto.setSubject(subject);
        emailDto.setText(text);
    
        rabbitTemplate.convertAndSend(queue, emailDto);
    }

    public void notifyOrientador(Monografia monografia) {
        Usuario orientador = monografia.getOrientador();
        String subject = "Nova Monografia para Revisão";
        String text = "Olá " + orientador.getNome() + ",\n\n" +
                      "O aluno " + monografia.getAluno().getNome() + " enviou uma nova monografia para revisão.\n\n" +
                      "Tema: " + monografia.getTema() + "\n" +
                      "Status: " + monografia.getStatus() + "\n\n" +
                      "Por favor, revise os documentos e aprove ou solicite correções.\n\n" +
                      "Atenciosamente,\n" +
                      "Equipe Utanga";
    
        sendEmail(orientador, subject, text);
    }
    
    public void notifyAdmin(Monografia monografia) {
        if (monografia.getAdmin() == null) {
            throw new RuntimeException("Nenhum administrador associado à monografia.");
        }
    
        Usuario admin = monografia.getAdmin();
        String subject = "Nova Monografia Aprovada";
        String text = "Olá " + admin.getNome() + ",\n\n" +
                      "A monografia do aluno " + monografia.getAluno().getNome() + " foi aprovada pelo orientador.\n\n" +
                      "Tema: " + monografia.getTema() + "\n" +
                      "Status: " + monografia.getStatus() + "\n\n" +
                      "Por favor, revise a monografia e tome as medidas necessárias.\n\n" +
                      "Atenciosamente,\n" +
                      "Equipe Utanga";
    
        sendEmail(admin, subject, text);
    }
    
    public void notifyAluno(Monografia monografia) {
        Usuario aluno = monografia.getAluno();
        String subject = "Correções Necessárias na Monografia";
        String text = "Olá " + aluno.getNome() + ",\n\n" +
                      "Sua monografia com o tema \"" + monografia.getTema() + "\" precisa de correções.\n\n" +
                      "Descrição das correções: " + monografia.getDescricaoMelhoria() + "\n\n" +
                      "Por favor, faça as correções necessárias e reenvie a monografia.\n\n" +
                      "Atenciosamente,\n" +
                      "Equipe Utanga";
    
        sendEmail(aluno, subject, text);
    }

    public void sendResetPasswordEmail(String email, String recoveryLink) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o email: " + email));
    
        String subject = "Recuperação de Senha";
        String text = "Olá,\n\nVocê solicitou a recuperação da sua senha. Acesse o link para redefinir sua senha: " + recoveryLink;
    
        sendEmail(usuario, subject, text);
    }

    public void sendEmailUser(Usuario usuario) {
        EmailDto emailDto = new EmailDto();
        emailDto.setUserId(usuario.getId());
        emailDto.setEmailFrom("pesselaabreu79@gmail.com"); // Defina o email de origem
        emailDto.setEmailTo(usuario.getEmail());
        emailDto.setSubject("🎉 Bem-vindo à Utanga, " + usuario.getNome() + "! 🎓");
        emailDto.setText("Olá " + usuario.getNome() + ",\n\n" +
                         "É com grande alegria que damos as boas-vindas a você na Utanga, " +
                         "a universidade onde o conhecimento transforma vidas! Estamos muito felizes por tê-lo conosco.\n\n" +
                         "Prepare-se para uma jornada incrível de aprendizado, conquistas e crescimento. " +
                         "Nossa equipe está à disposição para ajudá-lo a alcançar seus objetivos.\n\n" +
                         "Seja bem-vindo à nossa família acadêmica!\n\n" +
                         "Abraços,\n" +
                         "Equipe Utanga 🎓");
    
        rabbitTemplate.convertAndSend(queue, emailDto);
    }

    public void notifyPreDefesaMarcada(PreDefesa preDefesa) {
    // Notifica o aluno
    Usuario aluno = preDefesa.getMonografia().getAluno();
    String subjectAluno = "Pré-Defesa Marcada";
    String textAluno = "Olá " + aluno.getNome() + ",\n\n" +
                       "Sua pré-defesa foi marcada com sucesso.\n\n" +
                       "Tema: " + preDefesa.getMonografia().getTema() + "\n" +
                       "Data de Início: " + preDefesa.getDataInicio() + "\n" +
                       "Data de Término: " + preDefesa.getDataFim() + "\n\n" +
                       "Presidente: " + preDefesa.getPresidente().getNome() + "\n" +
                       "Vogal: " + preDefesa.getVogal().getNome() + "\n\n" +
                       "Atenciosamente,\n" +
                       "Equipe Utanga";
    sendEmail(aluno, subjectAluno, textAluno);

    // Notifica o orientador
    Usuario orientador = preDefesa.getMonografia().getOrientador();
    String subjectOrientador = "Pré-Defesa Marcada para o Aluno " + aluno.getNome();
    String textOrientador = "Olá " + orientador.getNome() + ",\n\n" +
                            "A pré-defesa do aluno " + aluno.getNome() + " foi marcada com sucesso.\n\n" +
                            "Tema: " + preDefesa.getMonografia().getTema() + "\n" +
                            "Data de Início: " + preDefesa.getDataInicio() + "\n" +
                            "Data de Término: " + preDefesa.getDataFim() + "\n\n" +
                            "Presidente: " + preDefesa.getPresidente().getNome() + "\n" +
                            "Vogal: " + preDefesa.getVogal().getNome() + "\n\n" +
                            "Atenciosamente,\n" +
                            "Equipe Utanga";
    sendEmail(orientador, subjectOrientador, textOrientador);

    // Notifica o presidente
    Usuario presidente = preDefesa.getPresidente();
    String subjectPresidente = "Pré-Defesa Marcada";
    String textPresidente = "Olá " + presidente.getNome() + ",\n\n" +
                            "Você foi designado como presidente da pré-defesa do aluno " + aluno.getNome() + ".\n\n" +
                            "Tema: " + preDefesa.getMonografia().getTema() + "\n" +
                            "Data de Início: " + preDefesa.getDataInicio() + "\n" +
                            "Data de Término: " + preDefesa.getDataFim() + "\n\n" +
                            "Atenciosamente,\n" +
                            "Equipe Utanga";
    sendEmail(presidente, subjectPresidente, textPresidente);

    // Notifica o vogal
    Usuario vogal = preDefesa.getVogal();
    String subjectVogal = "Pré-Defesa Marcada";
    String textVogal = "Olá " + vogal.getNome() + ",\n\n" +
                       "Você foi designado como vogal da pré-defesa do aluno " + aluno.getNome() + ".\n\n" +
                       "Tema: " + preDefesa.getMonografia().getTema() + "\n" +
                       "Data de Início: " + preDefesa.getDataInicio() + "\n" +
                       "Data de Término: " + preDefesa.getDataFim() + "\n\n" +
                       "Atenciosamente,\n" +
                       "Equipe Utanga";
    sendEmail(vogal, subjectVogal, textVogal);
}

public void notifyPreDefesaAprovada(PreDefesa preDefesa) {
    // Notifica o aluno
    Usuario aluno = preDefesa.getMonografia().getAluno();
    String subjectAluno = "Pré-Defesa Aprovada";
    String textAluno = "Olá " + aluno.getNome() + ",\n\n" +
                       "Sua pré-defesa foi aprovada com sucesso.\n\n" +
                       "Tema: " + preDefesa.getMonografia().getTema() + "\n" +
                       "Data de Início: " + preDefesa.getDataInicio() + "\n" +
                       "Data de Término: " + preDefesa.getDataFim() + "\n\n" +
                       "Atenciosamente,\n" +
                       "Equipe Utanga";
    sendEmail(aluno, subjectAluno, textAluno);

    // Notifica o orientador
    Usuario orientador = preDefesa.getMonografia().getOrientador();
    String subjectOrientador = "Pré-Defesa Aprovada para o Aluno " + aluno.getNome();
    String textOrientador = "Olá " + orientador.getNome() + ",\n\n" +
                            "A pré-defesa do aluno " + aluno.getNome() + " foi aprovada com sucesso.\n\n" +
                            "Tema: " + preDefesa.getMonografia().getTema() + "\n" +
                            "Data de Início: " + preDefesa.getDataInicio() + "\n" +
                            "Data de Término: " + preDefesa.getDataFim() + "\n\n" +
                            "Atenciosamente,\n" +
                            "Equipe Utanga";
    sendEmail(orientador, subjectOrientador, textOrientador);
}

public void notifyPreDefesaReprovada(PreDefesa preDefesa, String descricao) {
    // Notifica o aluno
    Usuario aluno = preDefesa.getMonografia().getAluno();
    String subjectAluno = "Pré-Defesa Reprovada";
    String textAluno = "Olá " + aluno.getNome() + ",\n\n" +
                       "Sua pré-defesa foi reprovada.\n\n" +
                       "Tema: " + preDefesa.getMonografia().getTema() + "\n" +
                       "Data de Início: " + preDefesa.getDataInicio() + "\n" +
                       "Data de Término: " + preDefesa.getDataFim() + "\n\n" +
                       "Descrição das correções: " + descricao + "\n\n" +
                       "Por favor, faça as correções necessárias e reenvie a monografia.\n\n" +
                       "Atenciosamente,\n" +
                       "Equipe Utanga";
    sendEmail(aluno, subjectAluno, textAluno);

    // Notifica o orientador
    Usuario orientador = preDefesa.getMonografia().getOrientador();
    String subjectOrientador = "Pré-Defesa Reprovada para o Aluno " + aluno.getNome();
    String textOrientador = "Olá " + orientador.getNome() + ",\n\n" +
                            "A pré-defesa do aluno " + aluno.getNome() + " foi reprovada.\n\n" +
                            "Tema: " + preDefesa.getMonografia().getTema() + "\n" +
                            "Data de Início: " + preDefesa.getDataInicio() + "\n" +
                            "Data de Término: " + preDefesa.getDataFim() + "\n\n" +
                            "Descrição das correções: " + descricao + "\n\n" +
                            "Atenciosamente,\n" +
                            "Equipe Utanga";
    sendEmail(orientador, subjectOrientador, textOrientador);
}
    

public void notifyDefesaMarcada(Defesa defesa) {
    // Notifica o aluno
    Usuario aluno = defesa.getMonografia().getAluno();
    String subjectAluno = "Defesa Marcada";
    String textAluno = "Olá " + aluno.getNome() + ",\n\n" +
                       "Sua defesa foi marcada com sucesso.\n\n" +
                       "Tema: " + defesa.getMonografia().getTema() + "\n" +
                       "Data de Início: " + defesa.getDataInicio() + "\n" +
                       "Data de Término: " + defesa.getDataFim() + "\n\n" +
                       "Presidente: " + defesa.getPresidente().getNome() + "\n" +
                       "Vogal: " + defesa.getVogal().getNome() + "\n\n" +
                       "Atenciosamente,\n" +
                       "Equipe Utanga";
    sendEmail(aluno, subjectAluno, textAluno);

    // Notifica o orientador
    Usuario orientador = defesa.getMonografia().getOrientador();
    String subjectOrientador = "Defesa Marcada para o Aluno " + aluno.getNome();
    String textOrientador = "Olá " + orientador.getNome() + ",\n\n" +
                            "A defesa do aluno " + aluno.getNome() + " foi marcada com sucesso.\n\n" +
                            "Tema: " + defesa.getMonografia().getTema() + "\n" +
                            "Data de Início: " + defesa.getDataInicio() + "\n" +
                            "Data de Término: " + defesa.getDataFim() + "\n\n" +
                            "Presidente: " + defesa.getPresidente().getNome() + "\n" +
                            "Vogal: " + defesa.getVogal().getNome() + "\n\n" +
                            "Atenciosamente,\n" +
                            "Equipe Utanga";
    sendEmail(orientador, subjectOrientador, textOrientador);
}

public void notifyDefesaAprovada(Defesa defesa) {
    // Notifica o aluno
    Usuario aluno = defesa.getMonografia().getAluno();
    String subjectAluno = "Defesa Aprovada";
    String textAluno = "Olá " + aluno.getNome() + ",\n\n" +
                       "Sua defesa foi aprovada com sucesso.\n\n" +
                       "Tema: " + defesa.getMonografia().getTema() + "\n" +
                       "Data de Início: " + defesa.getDataInicio() + "\n" +
                       "Data de Término: " + defesa.getDataFim() + "\n\n" +
                       "Nota: " + defesa.getNota() + "\n\n" +
                       "Atenciosamente,\n" +
                       "Equipe Utanga";
    sendEmail(aluno, subjectAluno, textAluno);

    // Notifica o orientador
    Usuario orientador = defesa.getMonografia().getOrientador();
    String subjectOrientador = "Defesa Aprovada para o Aluno " + aluno.getNome();
    String textOrientador = "Olá " + orientador.getNome() + ",\n\n" +
                            "A defesa do aluno " + aluno.getNome() + " foi aprovada com sucesso.\n\n" +
                            "Tema: " + defesa.getMonografia().getTema() + "\n" +
                            "Data de Início: " + defesa.getDataInicio() + "\n" +
                            "Data de Término: " + defesa.getDataFim() + "\n\n" +
                            "Nota: " + defesa.getNota() + "\n\n" +
                            "Atenciosamente,\n" +
                            "Equipe Utanga";
    sendEmail(orientador, subjectOrientador, textOrientador);
}

public void notifyDefesaReprovada(Defesa defesa, String descricao) {
    // Notifica o aluno
    Usuario aluno = defesa.getMonografia().getAluno();
    String subjectAluno = "Defesa Reprovada";
    String textAluno = "Olá " + aluno.getNome() + ",\n\n" +
                       "Sua defesa foi reprovada.\n\n" +
                       "Tema: " + defesa.getMonografia().getTema() + "\n" +
                       "Data de Início: " + defesa.getDataInicio() + "\n" +
                       "Data de Término: " + defesa.getDataFim() + "\n\n" +
                       "Descrição das correções: " + descricao + "\n\n" +
                       "Por favor, faça as correções necessárias e reenvie a monografia.\n\n" +
                       "Atenciosamente,\n" +
                       "Equipe Utanga";
    sendEmail(aluno, subjectAluno, textAluno);

    // Notifica o orientador
    Usuario orientador = defesa.getMonografia().getOrientador();
    String subjectOrientador = "Defesa Reprovada para o Aluno " + aluno.getNome();
    String textOrientador = "Olá " + orientador.getNome() + ",\n\n" +
                            "A defesa do aluno " + aluno.getNome() + " foi reprovada.\n\n" +
                            "Tema: " + defesa.getMonografia().getTema() + "\n" +
                            "Data de Início: " + defesa.getDataInicio() + "\n" +
                            "Data de Término: " + defesa.getDataFim() + "\n\n" +
                            "Descrição das correções: " + descricao + "\n\n" +
                            "Atenciosamente,\n" +
                            "Equipe Utanga";
    sendEmail(orientador, subjectOrientador, textOrientador);
}

public void notifyMonografiaCriada(Monografia monografia) {
    // Notifica o aluno
    Usuario aluno = monografia.getAluno();
    String subjectAluno = "Monografia Criada";
    String textAluno = "Olá " + aluno.getNome() + ",\n\n" +
                       "Sua monografia foi criada com sucesso.\n\n" +
                       "Tema: " + monografia.getTema() + "\n" +
                       "Status: " + monografia.getStatus() + "\n\n" +
                       "Atenciosamente,\n" +
                       "Equipe Utanga";
    sendEmail(aluno, subjectAluno, textAluno);

    // Notifica o orientador
    Usuario orientador = monografia.getOrientador();
    String subjectOrientador = "Monografia Criada para o Aluno " + aluno.getNome();
    String textOrientador = "Olá " + orientador.getNome() + ",\n\n" +
                            "A monografia do aluno " + aluno.getNome() + " foi criada com sucesso.\n\n" +
                            "Tema: " + monografia.getTema() + "\n" +
                            "Status: " + monografia.getStatus() + "\n\n" +
                            "Atenciosamente,\n" +
                            "Equipe Utanga";
    sendEmail(orientador, subjectOrientador, textOrientador);
}

public void notifyMonografiaAtualizada(Monografia monografia) {
    // Notifica o aluno
    Usuario aluno = monografia.getAluno();
    String subjectAluno = "Monografia Atualizada";
    String textAluno = "Olá " + aluno.getNome() + ",\n\n" +
                       "Sua monografia foi atualizada com sucesso.\n\n" +
                       "Tema: " + monografia.getTema() + "\n" +
                       "Status: " + monografia.getStatus() + "\n\n" +
                       "Atenciosamente,\n" +
                       "Equipe Utanga";
    sendEmail(aluno, subjectAluno, textAluno);

    // Notifica o orientador
    Usuario orientador = monografia.getOrientador();
    String subjectOrientador = "Monografia Atualizada para o Aluno " + aluno.getNome();
    String textOrientador = "Olá " + orientador.getNome() + ",\n\n" +
                            "A monografia do aluno " + aluno.getNome() + " foi atualizada com sucesso.\n\n" +
                            "Tema: " + monografia.getTema() + "\n" +
                            "Status: " + monografia.getStatus() + "\n\n" +
                            "Atenciosamente,\n" +
                            "Equipe Utanga";
    sendEmail(orientador, subjectOrientador, textOrientador);
}

public void notifyMonografiaAprovada(Monografia monografia) {
    // Notifica o aluno
    Usuario aluno = monografia.getAluno();
    String subjectAluno = "Monografia Aprovada";
    String textAluno = "Olá " + aluno.getNome() + ",\n\n" +
                       "Sua monografia foi aprovada com sucesso.\n\n" +
                       "Tema: " + monografia.getTema() + "\n" +
                       "Status: " + monografia.getStatus() + "\n\n" +
                       "Atenciosamente,\n" +
                       "Equipe Utanga";
    sendEmail(aluno, subjectAluno, textAluno);

    // Notifica o orientador
    Usuario orientador = monografia.getOrientador();
    String subjectOrientador = "Monografia Aprovada para o Aluno " + aluno.getNome();
    String textOrientador = "Olá " + orientador.getNome() + ",\n\n" +
                            "A monografia do aluno " + aluno.getNome() + " foi aprovada com sucesso.\n\n" +
                            "Tema: " + monografia.getTema() + "\n" +
                            "Status: " + monografia.getStatus() + "\n\n" +
                            "Atenciosamente,\n" +
                            "Equipe Utanga";
    sendEmail(orientador, subjectOrientador, textOrientador);
}

public void notifyMonografiaReprovada(Monografia monografia, String descricao) {
    // Notifica o aluno
    Usuario aluno = monografia.getAluno();
    String subjectAluno = "Monografia Reprovada";
    String textAluno = "Olá " + aluno.getNome() + ",\n\n" +
                       "Sua monografia foi reprovada.\n\n" +
                       "Tema: " + monografia.getTema() + "\n" +
                       "Status: " + monografia.getStatus() + "\n\n" +
                       "Descrição das correções: " + descricao + "\n\n" +
                       "Por favor, faça as correções necessárias e reenvie a monografia.\n\n" +
                       "Atenciosamente,\n" +
                       "Equipe Utanga";
    sendEmail(aluno, subjectAluno, textAluno);

    // Notifica o orientador
    Usuario orientador = monografia.getOrientador();
    String subjectOrientador = "Monografia Reprovada para o Aluno " + aluno.getNome();
    String textOrientador = "Olá " + orientador.getNome() + ",\n\n" +
                            "A monografia do aluno " + aluno.getNome() + " foi reprovada.\n\n" +
                            "Tema: " + monografia.getTema() + "\n" +
                            "Status: " + monografia.getStatus() + "\n\n" +
                            "Descrição das correções: " + descricao + "\n\n" +
                            "Atenciosamente,\n" +
                            "Equipe Utanga";
    sendEmail(orientador, subjectOrientador, textOrientador);
}

}
