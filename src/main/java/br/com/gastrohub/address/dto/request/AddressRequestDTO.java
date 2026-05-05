package br.com.gastrohub.address.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para cadastro de endereco")
public record AddressRequestDTO(

        @NotBlank(message = "CEP é obrigatório")
        @Pattern(regexp = "\\d{5}-?\\d{3}", message = "CEP deve estar no formato 00000-000")
        @Schema(description = "CEP do endereco", example = "01001-000")
        String cep,

        @NotBlank(message = "Rua é obrigatória")
        @Size(min = 3, max = 150, message = "Rua deve ter entre 3 e 150 caracteres")
        @Schema(description = "Rua, avenida ou logradouro", example = "Praca da Se")
        String rua,

        @NotBlank(message = "Número é obrigatório")
        @Size(max = 10, message = "Número deve ter no máximo 10 caracteres")
        @Schema(description = "Numero do endereco", example = "100")
        String numero,

        @NotBlank(message = "Cidade é obrigatória")
        @Size(min = 2, max = 100, message = "Cidade deve ter entre 2 e 100 caracteres")
        @Schema(description = "Cidade do endereco", example = "Sao Paulo")
        String cidade,

        @NotBlank(message = "Estado é obrigatório")
        @Size(min = 2, max = 100, message = "Estado deve ter entre 2 e 100 caracteres")
        @Schema(description = "Estado ou UF do endereco", example = "SP")
        String estado


) {}
