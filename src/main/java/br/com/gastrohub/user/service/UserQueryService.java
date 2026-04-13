package br.com.gastrohub.user.service;

import br.com.gastrohub.user.dto.response.UserResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserQueryService {
    UserResponseDTO findById(UUID id);
    Page<UserResponseDTO> findAll(Pageable pageable);
    Page<UserResponseDTO> searchByNome(String nome, Pageable pageable);
}
