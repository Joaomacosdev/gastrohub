package br.com.gastrohub.user.service;

import br.com.gastrohub.address.dto.request.AddressRequestDTO;
import br.com.gastrohub.infra.exception.NotFoundException;
import br.com.gastrohub.user.dto.request.UserRequestDTO;
import br.com.gastrohub.user.dto.request.UserUpdateDTO;
import br.com.gastrohub.user.dto.response.UserResponseDTO;
import br.com.gastrohub.user.entity.User;
import br.com.gastrohub.user.entity.enums.Role;
import br.com.gastrohub.user.event.UserCreatedEvent;
import br.com.gastrohub.user.mapper.UserMapper;
import br.com.gastrohub.user.repository.UserRepository;
import br.com.gastrohub.user.strategy.UserUpdateValidationStrategy;
import br.com.gastrohub.user.strategy.UserValidationStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper mapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    private UserServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new UserServiceImpl(
                userRepository,
                mapper,
                List.<UserValidationStrategy>of(),
                List.<UserUpdateValidationStrategy>of(),
                passwordEncoder,
                eventPublisher
        );
    }

    @Test
    void createUser_deveCriarUsuarioECodificarSenhaQuandoDadosValidos() {
        UserRequestDTO request = userRequest();
        User user = user();
        UserResponseDTO response = userResponse(user.getId());

        when(mapper.toEntity(request)).thenReturn(user);
        when(passwordEncoder.encode(request.senha())).thenReturn("senha-criptografada");
        when(userRepository.save(user)).thenReturn(user);
        when(mapper.toResponseDTO(user)).thenReturn(response);

        UserResponseDTO result = service.createUser(request);

        assertThat(result).isEqualTo(response);
        assertThat(user.getSenha()).isEqualTo("senha-criptografada");
        verify(passwordEncoder).encode("123456");
        verify(userRepository).save(user);
        verify(mapper).toResponseDTO(user);
    }

    @Test
    void createUser_devePublicarEventoUserCreatedAposSalvar() {
        UserRequestDTO request = userRequest();
        User user = user();
        UserResponseDTO response = userResponse(user.getId());

        when(mapper.toEntity(request)).thenReturn(user);
        when(passwordEncoder.encode(request.senha())).thenReturn("senha-criptografada");
        when(userRepository.save(user)).thenReturn(user);
        when(mapper.toResponseDTO(user)).thenReturn(response);

        service.createUser(request);

        ArgumentCaptor<Object> eventCaptor = ArgumentCaptor.forClass(Object.class);
        verify(eventPublisher).publishEvent(eventCaptor.capture());

        assertThat(eventCaptor.getValue())
                .isInstanceOfSatisfying(UserCreatedEvent.class, event ->
                        assertThat(event.user()).isEqualTo(user)
                );
    }

    @Test
    void findById_deveRetornarUsuarioQuandoExiste() {
        UUID id = UUID.randomUUID();
        User user = user(id);
        UserResponseDTO response = userResponse(id);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(mapper.toResponseDTO(user)).thenReturn(response);

        UserResponseDTO result = service.findById(id);

        assertThat(result).isEqualTo(response);
        verify(userRepository).findById(id);
        verify(mapper).toResponseDTO(user);
    }

    @Test
    void findById_deveLancarNotFoundExceptionQuandoNaoExiste() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(id.toString());
    }

    @Test
    void findAll_deveRetornarPaginaDeUsuariosMapeados() {
        Pageable pageable = PageRequest.of(0, 10);
        User user = user();
        UserResponseDTO response = userResponse(user.getId());

        when(userRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(user), pageable, 1));
        when(mapper.toResponseDTO(user)).thenReturn(response);

        Page<UserResponseDTO> result = service.findAll(pageable);

        assertThat(result.getContent()).containsExactly(response);
        assertThat(result.getTotalElements()).isEqualTo(1);
        verify(userRepository).findAll(pageable);
    }

    @Test
    void searchByNome_deveBuscarComContainsCaseInsensitive() {
        Pageable pageable = PageRequest.of(0, 10);
        User user = user();
        UserResponseDTO response = userResponse(user.getId());

        when(userRepository.findByNomeIgnoreCaseContaining("Cliente", pageable))
                .thenReturn(new PageImpl<>(List.of(user), pageable, 1));
        when(mapper.toResponseDTO(user)).thenReturn(response);

        Page<UserResponseDTO> result = service.searchByNome("Cliente", pageable);

        assertThat(result.getContent()).containsExactly(response);
        verify(userRepository).findByNomeIgnoreCaseContaining("Cliente", pageable);
    }

    @Test
    void updateUser_deveAtualizarPerfilQuandoUsuarioExiste() {
        UUID id = UUID.randomUUID();
        User user = user(id);
        UserUpdateDTO request = userUpdateRequest();
        UserResponseDTO response = userResponse(id);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(mapper.toResponseDTO(user)).thenReturn(response);

        UserResponseDTO result = service.updateUser(id, request);

        assertThat(result).isEqualTo(response);
        assertThat(user.getNome()).isEqualTo("Cliente Atualizado");
        assertThat(user.getEmail()).isEqualTo("cliente.atualizado@email.com");
        assertThat(user.getLogin()).isEqualTo("cliente01");
        assertThat(user.getRole()).isEqualTo(Role.ROLE_CLIENTE);
        verify(mapper).toResponseDTO(user);
    }

    @Test
    void updateUser_deveLancarNotFoundQuandoUsuarioNaoExiste() {
        UUID id = UUID.randomUUID();
        UserUpdateDTO request = userUpdateRequest();

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateUser(id, request))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(id.toString());
    }

    @Test
    void deleteUser_deveDeletarQuandoUsuarioExiste() {
        UUID id = UUID.randomUUID();
        User user = user(id);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        service.deleteUser(id);

        verify(userRepository).delete(user);
    }

    @Test
    void deleteUser_deveLancarNotFoundQuandoUsuarioNaoExiste() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.deleteUser(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(id.toString());

        verify(userRepository, never()).delete(org.mockito.ArgumentMatchers.any(User.class));
    }

    private UserRequestDTO userRequest() {
        return new UserRequestDTO(
                "Cliente Teste",
                "cliente@email.com",
                "cliente01",
                "123456",
                Role.ROLE_CLIENTE,
                List.of(addressRequest())
        );
    }

    private UserUpdateDTO userUpdateRequest() {
        return new UserUpdateDTO(
                "Cliente Atualizado",
                "cliente.atualizado@email.com",
                "cliente01",
                Role.ROLE_CLIENTE
        );
    }

    private AddressRequestDTO addressRequest() {
        return new AddressRequestDTO(
                "01001-000",
                "Praca da Se",
                "100",
                "Sao Paulo",
                "SP"
        );
    }

    private User user() {
        return user(UUID.randomUUID());
    }

    private User user(UUID id) {
        return new User()
                .setId(id)
                .setNome("Cliente Teste")
                .setEmail("cliente@email.com")
                .setLogin("cliente01")
                .setDataUltimaAlteracao(LocalDateTime.of(2026, 5, 1, 12, 0))
                .setRole(Role.ROLE_CLIENTE);
    }

    private UserResponseDTO userResponse(UUID id) {
        return new UserResponseDTO(
                id,
                "Cliente Teste",
                "cliente@email.com",
                "cliente01",
                LocalDateTime.of(2026, 5, 1, 12, 0),
                List.of(),
                Role.ROLE_CLIENTE
        );
    }
}
