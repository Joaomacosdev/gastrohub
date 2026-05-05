package br.com.gastrohub.user.controller;

import br.com.gastrohub.user.controller.docs.AuthControllerDocs;
import br.com.gastrohub.user.dto.request.LoginRequestDTO;
import br.com.gastrohub.user.dto.request.UpdatePasswordRequest;
import br.com.gastrohub.user.dto.response.AuthResponseDTO;
import br.com.gastrohub.user.service.AuthService;
import br.com.gastrohub.user.service.UserCommandService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController implements AuthControllerDocs {

    private final AuthService authService;
    private final UserCommandService userCommandService;

    public AuthController(AuthService authService, UserCommandService userCommandService) {
        this.authService = authService;
        this.userCommandService = userCommandService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        return ResponseEntity.ok(authService.login(loginRequestDTO));
    }
    @PatchMapping("/password/{userID}")
    public ResponseEntity<Void> updateUserPassword(@Valid @RequestBody UpdatePasswordRequest password, @PathVariable UUID userID) {
        userCommandService.updatePassword(password, userID);
        return ResponseEntity.noContent().build();
    }
}
