package br.com.gastrohub.notification.service;

import br.com.gastrohub.infra.exception.BusinessException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class EmailAsyncService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String emailOrigem;

    private static final String NOME_ENVIADOR = "Gastro-Hub";

    @Value("${app.url}")
    private String urlSite;


    public EmailAsyncService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async("taskExecutor")
    public void enviarEmail(String emailUsuario, String assunto, String conteudo) {

        try {
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    "UTF-8"
            );

            helper.setFrom(emailOrigem, NOME_ENVIADOR);
            helper.setTo(emailUsuario);
            helper.setSubject(assunto);
            helper.setText(conteudo, true);

            mailSender.send(message);

        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new BusinessException("Erro ao enviar email");
        }
    }

}
