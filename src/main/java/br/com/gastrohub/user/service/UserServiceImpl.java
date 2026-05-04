package br.com.gastrohub.user.service;

import br.com.gastrohub.infra.exception.NotFoundException;
import br.com.gastrohub.notification.service.EmailService;
import br.com.gastrohub.user.dto.request.UpdatePasswordRequest;
import br.com.gastrohub.user.dto.request.UserRequestDTO;
import br.com.gastrohub.user.dto.request.UserUpdateDTO;
import br.com.gastrohub.user.dto.response.UserResponseDTO;
import br.com.gastrohub.user.entity.User;
import br.com.gastrohub.user.event.UserCreatedEvent;
import br.com.gastrohub.user.mapper.UserMapper;
import br.com.gastrohub.user.repository.UserRepository;
import br.com.gastrohub.user.strategy.UserUpdateValidationStrategy;
import br.com.gastrohub.user.strategy.UserValidationStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserCommandService, UserQueryService {

    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final List<UserValidationStrategy> userValidators;
    private final List<UserUpdateValidationStrategy> userUpdateValidators;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl(UserRepository userRepository, UserMapper mapper, List<UserValidationStrategy> userValidators, List<UserUpdateValidationStrategy> userUpdateValidators, PasswordEncoder passwordEncoder, ApplicationEventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.userValidators = userValidators;
        this.userUpdateValidators = userUpdateValidators;
        this.passwordEncoder = passwordEncoder;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    @Override
    public UserResponseDTO createUser(UserRequestDTO dto) {
        logger.debug("Criando usuário com email: {}", dto.email());

        userValidators.forEach(v -> v.validate(dto));

        User user = mapper.toEntity(dto);
        user.changeSenha(passwordEncoder.encode(dto.senha()));

        User savedUser = userRepository.save(user);
        eventPublisher.publishEvent(new UserCreatedEvent(savedUser));
        return mapper.toResponseDTO(savedUser);
    }

    @Transactional(readOnly = true)
    @Override
    public UserResponseDTO findById(UUID id) {
        logger.debug("Buscando usuário por ID: {}", id);

        User user = searchUser(id);

        return mapper.toResponseDTO(user);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<UserResponseDTO> findAll(@PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        logger.info("Listando todos os usuários");

        logger.info("Listando usuários - página: {}, tamanho: {}", pageable.getPageNumber(), pageable.getPageSize());
        return userRepository.findAll(pageable).map(mapper::toResponseDTO);

    }

    @Transactional(readOnly = true)
    @Override
    public Page<UserResponseDTO> searchByNome(String nome, Pageable pageable) {
        logger.info("Buscando  usuários com nomes");

        return userRepository.findByNomeIgnoreCaseContaining(nome, pageable).map(mapper::toResponseDTO);
    }

    @Transactional
    @Override
    public UserResponseDTO updateUser(UUID id, UserUpdateDTO dto) {

        logger.info("Atualizando usuário ID: {}", id);

        User user = searchUser(id);

        userUpdateValidators.forEach(v -> v.validate(dto));

        user.updateProfile(dto.nome(),
                dto.email(),
                dto.login(),
                dto.role());

        return mapper.toResponseDTO(user);
    }

    @Transactional
    @Override
    public void deleteUser(UUID id) {
        logger.info("Deletando usuário ID: {}", id);

        User user = searchUser(id);
        userRepository.delete(user);
    }


    @Transactional
    @Override
    public void updatePassword(UpdatePasswordRequest updatePassword, UUID userID) {
        logger.info("Atualizando Senha de usuário");
        var userToUpdate = searchUser(userID);
        userToUpdate.changeSenha(passwordEncoder.encode(updatePassword.password()));
        userToUpdate.setDataUltimaAlteracao(LocalDateTime.now());
        userRepository.save(userToUpdate);
    }

    private User searchUser(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário", id));
    }
}
