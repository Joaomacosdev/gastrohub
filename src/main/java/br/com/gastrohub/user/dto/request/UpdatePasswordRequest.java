package br.com.gastrohub.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para atualizacao de senha do usuario")
public record UpdatePasswordRequest(
        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 6, message = "Senha deve ter no máximo 6 caracteres")
        @Schema(description = "Nova senha do usuario", example = "novaSenha123")
        String password
) {}
