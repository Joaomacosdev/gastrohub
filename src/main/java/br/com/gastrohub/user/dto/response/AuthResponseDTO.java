package br.com.gastrohub.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Resposta de autenticacao com dados do usuario e token JWT")
public record AuthResponseDTO(
        @Schema(description = "Identificador UUID do usuario autenticado", example = "7f9f0f1e-7a6f-4a45-8d2e-9f9f6f3b8a11")
        UUID id,
        @Schema(description = "Nome completo do usuario autenticado", example = "Cliente Teste")
        String nome,
        @Schema(description = "Email do usuario autenticado", example = "cliente@email.com")
        String email,
        @Schema(description = "Login usado na autenticacao", example = "cliente01")
        String login,
        @Schema(description = "Token JWT para envio no header Authorization", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjbGllbnRlMDEifQ.fake-signature")
        String token,
        @Schema(description = "Perfil de acesso do usuario autenticado", example = "ROLE_CLIENTE")
        String role
) {
}
