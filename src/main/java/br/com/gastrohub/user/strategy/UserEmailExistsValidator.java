package br.com.gastrohub.user.strategy;

import br.com.gastrohub.infra.exception.EmailAlreadyExistsException;
import br.com.gastrohub.user.dto.request.UserRequestDTO;
import br.com.gastrohub.user.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UserEmailExistsValidator implements UserValidationStrategy {

    private final UserRepository userRepository;

    public UserEmailExistsValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void validate(UserRequestDTO dto) {
        if (userRepository.existsByEmail(dto.email())){
            throw new EmailAlreadyExistsException(dto.email());

        }
    }
}
