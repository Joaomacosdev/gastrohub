package br.com.gastrohub.address.dto.request;

public record AddressUpdateDTO(
        String cep,
        String rua,
        String numero,
        String cidade,
        String estado
) {
}
