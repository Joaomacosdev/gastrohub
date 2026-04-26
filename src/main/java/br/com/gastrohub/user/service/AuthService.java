package br.com.gastrohub.user.service;

import br.com.gastrohub.user.dto.request.LoginRequestDTO;
import br.com.gastrohub.user.dto.response.AuthResponseDTO;

public interface AuthService {
    AuthResponseDTO login(LoginRequestDTO loginRequestDTO);
}
