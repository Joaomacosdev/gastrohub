package br.com.gastrohub.user.service;

import br.com.gastrohub.infra.exception.BusinessException;
import br.com.gastrohub.infra.security.JwtTokenProvider;
import br.com.gastrohub.user.dto.request.LoginRequestDTO;
import br.com.gastrohub.user.dto.response.AuthResponseDTO;
import br.com.gastrohub.user.entity.User;
import br.com.gastrohub.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
                          JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public AuthResponseDTO login(LoginRequestDTO loginRequestDTO) {
        User user = userRepository.findByLogin(loginRequestDTO.login())
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));

        if (!passwordEncoder.matches(loginRequestDTO.senha(), user.getSenha())) {
            throw new BusinessException("Senha inválida");
        }

        String token = jwtTokenProvider.generateToken(user.getLogin(), user.getRole().toString(), user.getId().toString());

        return new AuthResponseDTO(
                user.getId(),
                user.getNome(),
                user.getEmail(),
                user.getLogin(),
                token,
                user.getRole().toString()
        );
    }
}
