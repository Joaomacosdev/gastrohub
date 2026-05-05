package br.com.gastrohub.user.strategy;

import br.com.gastrohub.infra.exception.EmailAlreadyExistsException;
import br.com.gastrohub.user.dto.request.UserUpdateDTO;
import br.com.gastrohub.user.entity.enums.Role;
import br.com.gastrohub.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserEmailUpdateValidatorTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserEmailUpdateValidator validator;

    @Test
    void deveLancarExcecaoQuandoEmailJaExiste() {
        UserUpdateDTO dto = userUpdate();
        when(userRepository.existsByEmail(dto.email())).thenReturn(true);

        assertThatThrownBy(() -> validator.validate(dto))
                .isInstanceOf(EmailAlreadyExistsException.class)
                .hasMessageContaining(dto.email());

        verify(userRepository).existsByEmail(dto.email());
    }

    @Test
    void naoDeveLancarExcecaoQuandoEmailNaoExiste() {
        UserUpdateDTO dto = userUpdate();
        when(userRepository.existsByEmail(dto.email())).thenReturn(false);

        assertThatCode(() -> validator.validate(dto))
                .doesNotThrowAnyException();

        verify(userRepository).existsByEmail(dto.email());
    }

    private UserUpdateDTO userUpdate() {
        return new UserUpdateDTO(
                "Cliente Atualizado",
                "cliente.atualizado@email.com",
                "cliente01",
                Role.ROLE_CLIENTE
        );
    }
}
