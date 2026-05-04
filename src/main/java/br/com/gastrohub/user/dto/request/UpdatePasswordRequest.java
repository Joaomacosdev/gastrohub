package br.com.gastrohub.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para atualizacao de senha do usuario")
public record UpdatePasswordRequest(
        @Schema(description = "Nova senha do usuario", example = "novaSenha123")
        String password
) {}
