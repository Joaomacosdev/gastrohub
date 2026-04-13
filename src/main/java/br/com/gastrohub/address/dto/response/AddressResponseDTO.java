package br.com.gastrohub.address.dto.response;

import java.util.UUID;

public record AddressResponseDTO(
        UUID id,
        String cep,
        String rua,
        String numero,
        String cidade) {
}
