package br.com.gastrohub.user.strategy;

import br.com.gastrohub.user.dto.request.UserRequestDTO;

public interface UserValidationStrategy {
    void validate(UserRequestDTO dto);
}
