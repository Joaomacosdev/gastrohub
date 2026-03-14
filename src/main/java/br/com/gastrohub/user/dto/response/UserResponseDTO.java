package br.com.gastrohub.user.dto.response;

import br.com.gastrohub.address.dto.response.AddressResponseDTO;
import br.com.gastrohub.user.entity.enums.Role;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        String nome,
        String email,
        String login,
        LocalDateTime dataUltimaAlteracao,
        List<AddressResponseDTO> address,
        Role role
) {

}
