package br.com.gastrohub.user.dto.request;

import br.com.gastrohub.address.dto.request.AddressRequestDTO;
import br.com.gastrohub.user.entity.enums.Role;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

@Schema(description = "Dados para cadastro de usuario no GastroHub")
public record UserRequestDTO(

        @NotBlank(message = "Nome é obrigatório")
        @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
        @Schema(description = "Nome completo do usuario", example = "Cliente Teste")
        String nome,

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email inválido")
        @Schema(description = "Email do usuario", example = "cliente@email.com")
        String email,

        @NotBlank(message = "Login é obrigatório")
        @Size(min = 3, max = 50, message = "Login deve ter entre 3 e 50 caracteres")
        @Schema(description = "Login usado para autenticacao", example = "cliente01")
        String login,

        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
        @Schema(description = "Senha de acesso do usuario", example = "123456")
        String senha,

        @NotNull(message = "Role é obrigatória")
        @Schema(
                description = "Perfil de acesso do usuario",
                example = "ROLE_CLIENTE",
                allowableValues = {"ROLE_CLIENTE", "ROLE_DONO_RESTAURANTE"}
        )
        Role role,

        @NotNull(message = "Endereço é obrigatório")
        @Valid
        @ArraySchema(
                arraySchema = @Schema(description = "Enderecos vinculados ao usuario"),
                schema = @Schema(implementation = AddressRequestDTO.class)
        )
        List<AddressRequestDTO> address

) {
}
