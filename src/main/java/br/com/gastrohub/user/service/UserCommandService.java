package br.com.gastrohub.user.service;

import br.com.gastrohub.user.dto.request.UserRequestDTO;
import br.com.gastrohub.user.dto.request.UserUpdateDTO;
import br.com.gastrohub.user.dto.response.UserResponseDTO;

import java.util.UUID;

public interface UserCommandService {

    UserResponseDTO createUser(UserRequestDTO dto);
    UserResponseDTO updateUser(UUID id, UserUpdateDTO dto);
    void deleteUser(UUID id);

}
