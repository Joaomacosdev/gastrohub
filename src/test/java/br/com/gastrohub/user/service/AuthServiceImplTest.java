package br.com.gastrohub.user.service;

import br.com.gastrohub.infra.exception.BusinessException;
import br.com.gastrohub.infra.security.JwtTokenProvider;
import br.com.gastrohub.user.dto.request.LoginRequestDTO;
import br.com.gastrohub.user.dto.response.AuthResponseDTO;
import br.com.gastrohub.user.entity.User;
import br.com.gastrohub.user.entity.enums.Role;
import br.com.gastrohub.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthServiceImpl service;

    @Test
    void login_deveRetornarTokenQuandoCredenciaisValidas() {
        LoginRequestDTO request = new LoginRequestDTO("cliente01", "123456");
        User user = user();

        when(userRepository.findByLogin("cliente01")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("123456", user.getSenha())).thenReturn(true);
        when(jwtTokenProvider.generateToken(anyString(), anyString(), anyString()))
                .thenReturn("jwt-token-fake");

        AuthResponseDTO response = service.login(request);

        assertThat(response.token()).isEqualTo("jwt-token-fake");
        assertThat(response.login()).isEqualTo("cliente01");
        assertThat(response.role()).isEqualTo("ROLE_CLIENTE");
        verify(jwtTokenProvider).generateToken(
                user.getLogin(),
                user.getRole().toString(),
                user.getId().toString()
        );
    }

    @Test
    void login_deveLancarBusinessExceptionQuandoUsuarioNaoExiste() {
        LoginRequestDTO request = new LoginRequestDTO("inexistente", "123456");
        when(userRepository.findByLogin("inexistente")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.login(request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("encontrado");

        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(jwtTokenProvider, never()).generateToken(anyString(), anyString(), anyString());
    }

    @Test
    void login_deveLancarBusinessExceptionQuandoSenhaNaoConfere() {
        LoginRequestDTO request = new LoginRequestDTO("cliente01", "senhaErrada");
        User user = user();

        when(userRepository.findByLogin("cliente01")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("senhaErrada", user.getSenha())).thenReturn(false);

        assertThatThrownBy(() -> service.login(request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("inv");

        verify(jwtTokenProvider, never()).generateToken(anyString(), anyString(), anyString());
    }

    private User user() {
        User user = new User()
                .setId(UUID.randomUUID())
                .setNome("Cliente Teste")
                .setEmail("cliente@email.com")
                .setLogin("cliente01")
                .setDataUltimaAlteracao(LocalDateTime.of(2026, 5, 1, 12, 0))
                .setRole(Role.ROLE_CLIENTE);
        user.changeSenha("senha-criptografada");
        return user;
    }
}
