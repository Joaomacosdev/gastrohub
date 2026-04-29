package br.com.gastrohub.user.dto.response;

import java.util.UUID;

public record AuthResponseDTO(
        UUID id,
        String nome,
        String email,
        String login,
        String token,
        String role
) {
}
