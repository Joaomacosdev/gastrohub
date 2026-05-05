package br.com.gastrohub.user.controller;

import br.com.gastrohub.user.controller.docs.UserControllerDocs;
import br.com.gastrohub.user.dto.request.UserRequestDTO;
import br.com.gastrohub.user.dto.request.UserUpdateDTO;
import br.com.gastrohub.user.dto.response.UserResponseDTO;
import br.com.gastrohub.user.service.UserCommandService;
import br.com.gastrohub.user.service.UserQueryService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController implements UserControllerDocs {

    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;

    public UserController(UserCommandService userCommandService, UserQueryService userQueryService) {
        this.userCommandService = userCommandService;
        this.userQueryService = userQueryService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO dto, UriComponentsBuilder uriBuilder) {
        UserResponseDTO userResponse = userCommandService.createUser(dto);
        URI uri = uriBuilder.path("/{id}").buildAndExpand(userResponse.id()).toUri();
        return ResponseEntity.created(uri).body(userResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok().body(userQueryService.findById(id));
    }

    @GetMapping
    public ResponseEntity<Page<UserResponseDTO>> findAll(@PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        return ResponseEntity.ok().body(userQueryService.findAll(pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<UserResponseDTO>> searchByNome(
            @RequestParam(required = false) String nome,
            @PageableDefault(size = 10) Pageable pageable) {

        return ResponseEntity.ok(userQueryService.searchByNome(nome, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable UUID id, @Valid @RequestBody UserUpdateDTO dto) {
        return ResponseEntity.ok().body(userCommandService.updateUser(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userCommandService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
