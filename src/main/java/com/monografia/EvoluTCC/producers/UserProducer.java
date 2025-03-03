package com.monografia.EvoluTCC.producers;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.monografia.EvoluTCC.dto.EmailDto;
import com.monografia.EvoluTCC.models.Monografia;
import com.monografia.EvoluTCC.models.Usuario;

@Component
public class UserProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.queue}")
    private String queue;

    public void sendEmail(Usuario usuario) {
        EmailDto emailDto = new EmailDto();
        emailDto.setUserId(usuario.getId());
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

    public void notifyOrientador(Monografia monografia) {
        EmailDto emailDto = new EmailDto();
        emailDto.setEmailTo(monografia.getOrientador().getEmail());
        emailDto.setSubject("Nova Monografia para Revisão");
        emailDto.setText("Olá " + monografia.getOrientador().getNome() + ",\n\n" +
                         "O aluno " + monografia.getAluno().getNome() + " enviou uma nova monografia para revisão.\n\n" +
                         "Tema: " + monografia.getTema() + "\n" +
                         "Status: " + monografia.getStatus() + "\n\n" +
                         "Por favor, revise os documentos e aprove ou solicite correções.\n\n" +
                         "Atenciosamente,\n" +
                         "Equipe Utanga");

        rabbitTemplate.convertAndSend(queue, emailDto);
    }

    public void notifyAdmin(Monografia monografia) {
        if (monografia.getAdmin() == null) {
            throw new RuntimeException("Nenhum administrador associado à monografia.");
        }
    
        EmailDto emailDto = new EmailDto();
        emailDto.setEmailTo(monografia.getAdmin().getEmail());
        emailDto.setSubject("Nova Monografia Aprovada");
        emailDto.setText("Olá " + monografia.getAdmin().getNome() + ",\n\n" +
                         "A monografia do aluno " + monografia.getAluno().getNome() + " foi aprovada pelo orientador.\n\n" +
                         "Tema: " + monografia.getTema() + "\n" +
                         "Status: " + monografia.getStatus() + "\n\n" +
                         "Por favor, revise a monografia e tome as medidas necessárias.\n\n" +
                         "Atenciosamente,\n" +
                         "Equipe Utanga");
    
        rabbitTemplate.convertAndSend(queue, emailDto);
    }
    
  

    public void notifyAluno(Monografia monografia) {
        EmailDto emailDto = new EmailDto();
        emailDto.setEmailTo(monografia.getAluno().getEmail());
        emailDto.setSubject("Correções Necessárias na Monografia");
        emailDto.setText("Olá " + monografia.getAluno().getNome() + ",\n\n" +
                         "Sua monografia com o tema \"" + monografia.getTema() + "\" precisa de correções.\n\n" +
                         "Descrição das correções: " + monografia.getDescricaoMelhoria() + "\n\n" +
                         "Por favor, faça as correções necessárias e reenvie a monografia.\n\n" +
                         "Atenciosamente,\n" +
                         "Equipe Utanga");

        rabbitTemplate.convertAndSend(queue, emailDto);
    }

    public void sendResetPasswordEmail(String email, String recoveryLink) {
        EmailDto emailDto = new EmailDto();
        emailDto.setEmailTo(email);
        emailDto.setSubject("Recuperação de Senha");
        emailDto.setText("Olá,\n\nVocê solicitou a recuperação da sua senha. Acesse o link para redefinir sua senha: " + recoveryLink);

        rabbitTemplate.convertAndSend(queue, emailDto);
    }

}
