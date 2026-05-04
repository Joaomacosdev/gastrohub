package br.com.gastrohub.user.strategy;

import br.com.gastrohub.infra.exception.LoginAlreadyExistsException;
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
class UserLoginUpdateValidatorTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserLoginUpdateValidator validator;

    @Test
    void deveLancarExcecaoQuandoLoginJaExiste() {
        UserUpdateDTO dto = userUpdate();
        when(userRepository.existsByLogin(dto.login())).thenReturn(true);

        assertThatThrownBy(() -> validator.validate(dto))
                .isInstanceOf(LoginAlreadyExistsException.class)
                .hasMessageContaining(dto.login());

        verify(userRepository).existsByLogin(dto.login());
    }

    @Test
    void naoDeveLancarExcecaoQuandoLoginNaoExiste() {
        UserUpdateDTO dto = userUpdate();
        when(userRepository.existsByLogin(dto.login())).thenReturn(false);

        assertThatCode(() -> validator.validate(dto))
                .doesNotThrowAnyException();

        verify(userRepository).existsByLogin(dto.login());
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
