package br.com.gastrohub.user.event;

import br.com.gastrohub.user.entity.User;

public record UserCreatedEvent(User user) {
}
