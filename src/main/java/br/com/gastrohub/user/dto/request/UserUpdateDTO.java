package br.com.gastrohub.user.dto.request;

import br.com.gastrohub.user.entity.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para atualizacao cadastral do usuario")
public record UserUpdateDTO(
        @NotBlank(message = "Nome é obrigatório")
        @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
        @Schema(description = "Nome completo atualizado do usuario", example = "Cliente Atualizado")
        String nome,

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email inválido")
        @Schema(description = "Email atualizado do usuario", example = "cliente.atualizado@email.com")
        String email,

        @NotBlank(message = "Login é obrigatório")
        @Size(min = 3, max = 50, message = "Login deve ter entre 3 e 50 caracteres")
        @Schema(description = "Login atualizado ou mantido para autenticacao", example = "cliente01")
        String login,

        @NotNull(message = "Role é obrigatória")
        @Schema(
                description = "Perfil de acesso atualizado do usuario",
                example = "ROLE_CLIENTE",
                allowableValues = {"ROLE_CLIENTE", "ROLE_DONO_RESTAURANTE"}
        )
        Role role


) {
}
