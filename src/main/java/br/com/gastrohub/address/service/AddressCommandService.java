package br.com.gastrohub.address.service;

import br.com.gastrohub.address.dto.request.AddressRequestDTO;
import br.com.gastrohub.address.dto.request.AddressUpdateDTO;
import br.com.gastrohub.address.dto.response.AddressResponseDTO;

import java.util.UUID;

public interface AddressCommandService {
    AddressResponseDTO createAddress(AddressRequestDTO dto);
    AddressResponseDTO updateAddress(UUID id, AddressUpdateDTO dto);
    void deleteAddress(UUID id);
}
