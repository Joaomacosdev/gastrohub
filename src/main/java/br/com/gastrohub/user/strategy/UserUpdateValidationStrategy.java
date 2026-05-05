package br.com.gastrohub.user.strategy;

import br.com.gastrohub.user.dto.request.UserUpdateDTO;

public interface UserUpdateValidationStrategy {
    void validate(UserUpdateDTO dto);

}
