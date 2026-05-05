package br.com.gastrohub.user.strategy;

import br.com.gastrohub.infra.exception.LoginAlreadyExistsException;
import br.com.gastrohub.user.dto.request.UserRequestDTO;
import br.com.gastrohub.user.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UserLoginExistsValidator implements UserValidationStrategy {

    private final UserRepository userRepository;

    public UserLoginExistsValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void validate(UserRequestDTO dto) {
        if (userRepository.existsByLogin(dto.login())){
            throw new LoginAlreadyExistsException(dto.login());

        }
    }
}
