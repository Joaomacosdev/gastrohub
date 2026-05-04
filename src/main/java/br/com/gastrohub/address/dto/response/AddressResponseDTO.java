package br.com.gastrohub.address.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Dados de resposta com informacoes do endereco")
public record AddressResponseDTO(
        @Schema(description = "Identificador UUID do endereco", example = "b9d13e5d-6b0d-4b8f-8d8d-52d92a8848ab")
        UUID id,
        @Schema(description = "CEP do endereco", example = "01001-000")
        String cep,
        @Schema(description = "Rua, avenida ou logradouro", example = "Praca da Se")
        String rua,
        @Schema(description = "Numero do endereco", example = "100")
        String numero,
        @Schema(description = "Cidade do endereco", example = "Sao Paulo")
        String cidade,
        @Schema(description = "Estado ou UF do endereco", example = "SP")
        String estado) {
}
