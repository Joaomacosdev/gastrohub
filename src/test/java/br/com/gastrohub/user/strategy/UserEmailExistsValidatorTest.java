package br.com.gastrohub.user.strategy;

import br.com.gastrohub.address.dto.request.AddressRequestDTO;
import br.com.gastrohub.infra.exception.EmailAlreadyExistsException;
import br.com.gastrohub.user.dto.request.UserRequestDTO;
import br.com.gastrohub.user.entity.enums.Role;
import br.com.gastrohub.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserEmailExistsValidatorTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserEmailExistsValidator validator;

    @Test
    void deveLancarExcecaoQuandoEmailJaExiste() {
        UserRequestDTO dto = userRequest();
        when(userRepository.existsByEmail(dto.email())).thenReturn(true);

        assertThatThrownBy(() -> validator.validate(dto))
                .isInstanceOf(EmailAlreadyExistsException.class)
                .hasMessageContaining(dto.email());

        verify(userRepository).existsByEmail(dto.email());
    }

    @Test
    void naoDeveLancarExcecaoQuandoEmailNaoExiste() {
        UserRequestDTO dto = userRequest();
        when(userRepository.existsByEmail(dto.email())).thenReturn(false);

        assertThatCode(() -> validator.validate(dto))
                .doesNotThrowAnyException();

        verify(userRepository).existsByEmail(dto.email());
    }

    private UserRequestDTO userRequest() {
        return new UserRequestDTO(
                "Cliente Teste",
                "cliente@email.com",
                "cliente01",
                "123456",
                Role.ROLE_CLIENTE,
                List.of(new AddressRequestDTO("01001-000", "Praca da Se", "100", "Sao Paulo", "SP"))
        );
    }
}
