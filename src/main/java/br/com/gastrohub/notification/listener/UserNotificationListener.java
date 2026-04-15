package br.com.gastrohub.notification.listener;

import br.com.gastrohub.notification.service.EmailService;
import br.com.gastrohub.user.event.UserCreatedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class UserNotificationListener {
    private final EmailService emailService;

    public UserNotificationListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @Async
    @EventListener
    public void handleUserCreated(UserCreatedEvent event) {
        emailService.enviarEmailVerificacao(event.user());
    }
}
