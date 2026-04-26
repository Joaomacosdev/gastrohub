package br.com.gastrohub.notification.entity;

import br.com.gastrohub.notification.entity.enums.NotificationType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String subject;

    @NotBlank(message = "recipient is required")
    private String recipient;

    @Lob
    private String body;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private LocalDateTime createdAt = LocalDateTime.now();

    private boolean isHtml;

    public Notification() {
    }

    public Notification(String subject, String recipient, String body, NotificationType type, LocalDateTime createdAt, boolean isHtml) {
        this.subject = subject;
        this.recipient = recipient;
        this.body = body;
        this.type = type;
        this.createdAt = createdAt;
        this.isHtml = isHtml;
    }

    public UUID getId() {
        return id;
    }

    public Notification setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getSubject() {
        return subject;
    }

    public Notification setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public @NotBlank(message = "recipient is required") String getRecipient() {
        return recipient;
    }

    public Notification setRecipient(@NotBlank(message = "recipient is required") String recipient) {
        this.recipient = recipient;
        return this;
    }

    public String getBody() {
        return body;
    }

    public Notification setBody(String body) {
        this.body = body;
        return this;
    }

    public NotificationType getType() {
        return type;
    }

    public Notification setType(NotificationType type) {
        this.type = type;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Notification setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public boolean isHtml() {
        return isHtml;
    }

    public Notification setHtml(boolean html) {
        isHtml = html;
        return this;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Notification that = (Notification) object;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", subject='" + subject + '\'' +
                ", recipient='" + recipient + '\'' +
                ", body='" + body + '\'' +
                ", type=" + type +
                ", createdAt=" + createdAt +
                ", isHtml=" + isHtml +
                '}';
    }
}
