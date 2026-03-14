package br.com.gastrohub.address.service;

import br.com.gastrohub.address.dto.response.AddressResponseDTO;

import java.util.List;
import java.util.UUID;

public interface AddressQueryService {
    AddressResponseDTO findById(UUID id);
    List<AddressResponseDTO> findAll();
}
