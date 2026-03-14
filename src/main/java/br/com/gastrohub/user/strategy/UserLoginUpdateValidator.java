package br.com.gastrohub.user.strategy;

import br.com.gastrohub.infra.exception.LoginAlreadyExistsException;
import br.com.gastrohub.user.dto.request.UserUpdateDTO;
import br.com.gastrohub.user.repository.UserRepository;

public class UserLoginUpdateValidator implements UserUpdateValidationStrategy{

    private final UserRepository userRepository;

    public UserLoginUpdateValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public void validate(UserUpdateDTO dto) {
        if (userRepository.existsByLogin(dto.login())){
            throw new LoginAlreadyExistsException(dto.login());

        }
    }
}
