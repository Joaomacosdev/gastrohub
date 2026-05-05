package br.com.gastrohub.notification.service;

import br.com.gastrohub.user.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final EmailAsyncService emailAsyncService;

    @Value("${app.url}")
    private String urlSite;

    public EmailService(EmailAsyncService emailAsyncService) {
        this.emailAsyncService = emailAsyncService;
    }

    public void enviarEmailVerificacao(User user) {

        String assunto = "Bem-vindo ao Gastro-Hub";

        String conteudo = gerarConteudoEmail(
                """
              <div style="font-family: Arial, Helvetica, sans-serif; background-color:#f4f6f8; padding:20px;">
            <div style="max-width:600px; margin:auto; background:#ffffff; border-radius:8px; overflow:hidden;">
                
                <div style="background:#0d6efd; padding:20px; text-align:center;">
                    <h1 style="color:#ffffff; margin:0;">GastroHub</h1>
                </div>

                <div style="padding:30px; color:#333333;">
                    <p>Olá <strong>[[name]]</strong>,</p>

                    <p>
                        Sua conta no <strong>GastroHub</strong> foi criada com sucesso.
                        Agora você já pode acessar a plataforma e aproveitar nossos recursos de gerenciamento de usuários e restaurantes.
                    </p>

                    <p>
                        Abaixo estão seus dados de acesso:
                    </p>

                    <div style="background:#f1f3f5; padding:15px; border-radius:6px;">
                        <p><strong>Login:</strong> [[email]]</p>
                    </div>

                   

                    <div style="text-align:center; margin:30px 0;">
                        <a href="[[URL]]"
                           style="
                               background:#0d6efd;
                               color:#ffffff;
                               padding:14px 28px;
                               text-decoration:none;
                               border-radius:6px;
                               font-weight:bold;
                               display:inline-block;
                           ">
                            ACESSAR GASTROHUB
                        </a>
                    </div>

                    <p>
                        Caso tenha qualquer dúvida, nossa equipe está pronta para ajudar.
                    </p>

                    <p>
                        Atenciosamente,<br>
                        <strong>Equipe GastroHub</strong>
                    </p>
                </div>

            </div>
        </div>
        """,
                user.getNome(),
                urlSite,
                user.getEmail()
        );

        emailAsyncService.enviarEmail(user.getEmail(), assunto, conteudo);
    }

    private String gerarConteudoEmail(
            String template,
            String nome,
            String url,
            String email
    ) {
        return template
                .replace("[[name]]", nome)
                .replace("[[URL]]", url)
                .replace("[[email]]", email);
    }


}
