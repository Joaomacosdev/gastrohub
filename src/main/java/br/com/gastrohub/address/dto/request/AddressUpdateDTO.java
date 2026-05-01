package br.com.gastrohub.address.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para atualizacao de endereco")
public record AddressUpdateDTO(
        @Schema(description = "CEP atualizado do endereco", example = "01311-000")
        String cep,
        @Schema(description = "Rua, avenida ou logradouro atualizado", example = "Rua Atualizada")
        String rua,
        @Schema(description = "Numero atualizado do endereco", example = "200")
        String numero,
        @Schema(description = "Cidade atualizada do endereco", example = "Sao Paulo")
        String cidade,
        @Schema(description = "Estado ou UF atualizado do endereco", example = "SP")
        String estado
) {
}
