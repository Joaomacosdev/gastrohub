package br.com.gastrohub.user.dto.response;

import br.com.gastrohub.address.dto.response.AddressResponseDTO;
import br.com.gastrohub.user.entity.enums.Role;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Schema(description = "Dados de resposta com informacoes do usuario cadastrado")
public record UserResponseDTO(
        @Schema(description = "Identificador UUID do usuario", example = "7f9f0f1e-7a6f-4a45-8d2e-9f9f6f3b8a11")
        UUID id,
        @Schema(description = "Nome completo do usuario", example = "Cliente Teste")
        String nome,
        @Schema(description = "Email do usuario", example = "cliente@email.com")
        String email,
        @Schema(description = "Login usado para autenticacao", example = "cliente01")
        String login,
        @Schema(description = "Data e hora da ultima alteracao cadastral", example = "2026-05-01T12:34:56.789")
        LocalDateTime dataUltimaAlteracao,
        @ArraySchema(
                arraySchema = @Schema(description = "Enderecos vinculados ao usuario"),
                schema = @Schema(implementation = AddressResponseDTO.class)
        )
        List<AddressResponseDTO> address,
        @Schema(
                description = "Perfil de acesso do usuario",
                example = "ROLE_CLIENTE",
                allowableValues = {"ROLE_CLIENTE", "ROLE_DONO_RESTAURANTE"}
        )
        Role role
) {

}
