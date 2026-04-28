package br.com.gastrohub.address.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record AddressRequestDTO(

        @NotBlank(message = "CEP é obrigatório")
        @Pattern(regexp = "\\d{5}-?\\d{3}", message = "CEP deve estar no formato 00000-000")
        String cep,

        @NotBlank(message = "Rua é obrigatória")
        @Size(min = 3, max = 150, message = "Rua deve ter entre 3 e 150 caracteres")
        String rua,

        @NotBlank(message = "Número é obrigatório")
        @Size(max = 10, message = "Número deve ter no máximo 10 caracteres")
        String numero,

        @NotBlank(message = "Cidade é obrigatória")
        @Size(min = 2, max = 100, message = "Cidade deve ter entre 2 e 100 caracteres")
        String cidade,
        UUID id,

        @NotBlank(message = "Estado é obrigatório")
        @Size(min = 2, max = 100, message = "Estado deve ter entre 2 e 100 caracteres")
        String estado


) {}