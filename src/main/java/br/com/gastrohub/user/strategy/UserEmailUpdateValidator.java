package br.com.gastrohub.user.strategy;


import br.com.gastrohub.infra.exception.EmailAlreadyExistsException;
import br.com.gastrohub.user.dto.request.UserUpdateDTO;
import br.com.gastrohub.user.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UserEmailUpdateValidator implements UserUpdateValidationStrategy{

    private final UserRepository userRepository;

    public UserEmailUpdateValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public void validate(UserUpdateDTO dto) {
        if (userRepository.existsByEmail(dto.email())){
            throw new EmailAlreadyExistsException(dto.email());
        }
    }
}
