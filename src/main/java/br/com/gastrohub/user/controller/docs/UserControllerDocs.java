package br.com.gastrohub.user.controller.docs;

import br.com.gastrohub.user.dto.request.UserRequestDTO;
import br.com.gastrohub.user.dto.request.UserUpdateDTO;
import br.com.gastrohub.user.dto.response.UserResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@Tag(
        name = "Usuários",
        description = "Operações de gerenciamento de usuários (cliente e dono de restaurante)"
)
public interface UserControllerDocs {

    @Operation(
            summary = "Cadastrar usuário",
            description = "Cria um novo usuário cliente ou dono de restaurante, com endereço inicial vinculado ao cadastro."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Usuário cadastrado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Usuário criado",
                                    value = """
                                            {
                                              "id": "7f9f0f1e-7a6f-4a45-8d2e-9f9f6f3b8a11",
                                              "nome": "Vinicius Oliveira",
                                              "email": "vini@gastrohub.com",
                                              "login": "vinicius",
                                              "dataUltimaAlteracao": "2026-05-01T12:34:56.789",
                                              "address": [
                                                {
                                                  "id": "b9d13e5d-6b0d-4b8f-8d8d-52d92a8848ab",
                                                  "cep": "01001-000",
                                                  "rua": "Praca da Se",
                                                  "numero": "100",
                                                  "cidade": "Sao Paulo",
                                                  "estado": "SP"
                                                }
                                              ],
                                              "role": "ROLE_CLIENTE"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados de entrada inválidos",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    name = "Validacao",
                                    value = """
                                            {
                                              "type": "https://api.gastrohub.com/errors/VALIDATION_ERROR",
                                              "title": "Erro de validação",
                                              "status": 400,
                                              "detail": "Email inválido",
                                              "instance": "/api/v1/users",
                                              "code": "VALIDATION_ERROR",
                                              "timestamp": "2026-05-01T12:34:56.789Z"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Email ou login já cadastrado",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Email já cadastrado",
                                            value = """
                                                    {
                                                      "type": "https://api.gastrohub.com/errors/EMAIL_ALREADY_EXISTS",
                                                      "title": "Email já cadastrado",
                                                      "status": 409,
                                                      "detail": "Email já cadastrado: vini@gastrohub.com",
                                                      "instance": "/api/v1/users",
                                                      "code": "EMAIL_ALREADY_EXISTS",
                                                      "timestamp": "2026-05-01T12:34:56.789Z"
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Login já cadastrado",
                                            value = """
                                                    {
                                                      "type": "https://api.gastrohub.com/errors/LOGIN_ALREADY_EXISTS",
                                                      "title": "Login já cadastrado",
                                                      "status": 409,
                                                      "detail": "vinicius",
                                                      "instance": "/api/v1/users",
                                                      "code": "LOGIN_ALREADY_EXISTS",
                                                      "timestamp": "2026-05-01T12:34:56.789Z"
                                                    }
                                                    """
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Regra de negócio violada",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    name = "Regra de negocio",
                                    value = """
                                            {
                                              "type": "https://api.gastrohub.com/errors/BUSINESS_RULE_VIOLATION",
                                              "title": "Erro de regra de negócio",
                                              "status": 422,
                                              "detail": "Dados do usuário violam uma regra de negócio",
                                              "instance": "/api/v1/users",
                                              "code": "BUSINESS_RULE_VIOLATION",
                                              "timestamp": "2026-05-01T12:34:56.789Z"
                                            }
                                            """
                            )
                    )
            )
    })
    ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO dto, UriComponentsBuilder uriBuilder);

    @Operation(
            summary = "Buscar usuário por ID",
            description = "Retorna os dados cadastrais de um usuário pelo identificador UUID."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuário encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Usuário encontrado",
                                    value = """
                                            {
                                              "id": "7f9f0f1e-7a6f-4a45-8d2e-9f9f6f3b8a11",
                                              "nome": "Vinicius Oliveira",
                                              "email": "vini@gastrohub.com",
                                              "login": "vinicius",
                                              "dataUltimaAlteracao": "2026-05-01T12:34:56.789",
                                              "address": [
                                                {
                                                  "id": "b9d13e5d-6b0d-4b8f-8d8d-52d92a8848ab",
                                                  "cep": "01001-000",
                                                  "rua": "Praca da Se",
                                                  "numero": "100",
                                                  "cidade": "Sao Paulo",
                                                  "estado": "SP"
                                                }
                                              ],
                                              "role": "ROLE_CLIENTE"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário não encontrado",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    name = "Nao encontrado",
                                    value = """
                                            {
                                              "type": "https://api.gastrohub.com/errors/NOT_FOUND",
                                              "title": "Recurso não encontrado",
                                              "status": 404,
                                              "detail": "Usuário não encontrado com id: 7f9f0f1e-7a6f-4a45-8d2e-9f9f6f3b8a11",
                                              "instance": "/api/v1/users/7f9f0f1e-7a6f-4a45-8d2e-9f9f6f3b8a11",
                                              "code": "NOT_FOUND",
                                              "timestamp": "2026-05-01T12:34:56.789Z"
                                            }
                                            """
                            )
                    )
            )
    })
    ResponseEntity<UserResponseDTO> findById(
            @Parameter(description = "Identificador UUID do usuário", example = "7f9f0f1e-7a6f-4a45-8d2e-9f9f6f3b8a11")
            @PathVariable UUID id
    );

    @Operation(
            summary = "Listar usuários",
            description = "Lista usuários de forma paginada, com tamanho padrão 10 e ordenação por nome."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Página de usuários retornada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Página de usuários",
                                    value = """
                                            {
                                              "content": [
                                                {
                                                  "id": "7f9f0f1e-7a6f-4a45-8d2e-9f9f6f3b8a11",
                                                  "nome": "Vinicius Oliveira",
                                                  "email": "vini@gastrohub.com",
                                                  "login": "vinicius",
                                                  "dataUltimaAlteracao": "2026-05-01T12:34:56.789",
                                                  "address": [],
                                                  "role": "ROLE_CLIENTE"
                                                }
                                              ],
                                              "pageable": {
                                                "pageNumber": 0,
                                                "pageSize": 10
                                              },
                                              "totalElements": 1,
                                              "totalPages": 1,
                                              "last": true,
                                              "first": true,
                                              "size": 10,
                                              "number": 0
                                            }
                                            """
                            )
                    )
            )
    })
    ResponseEntity<Page<UserResponseDTO>> findAll(@PageableDefault(size = 10, sort = "nome") Pageable pageable);

    @Operation(
            summary = "Pesquisar usuários por nome",
            description = "Busca usuários por trecho do nome, ignorando diferenças entre maiúsculas e minúsculas."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Pesquisa executada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Resultado da pesquisa",
                                    value = """
                                            {
                                              "content": [
                                                {
                                                  "id": "7f9f0f1e-7a6f-4a45-8d2e-9f9f6f3b8a11",
                                                  "nome": "Vinicius Oliveira",
                                                  "email": "vini@gastrohub.com",
                                                  "login": "vinicius",
                                                  "dataUltimaAlteracao": "2026-05-01T12:34:56.789",
                                                  "address": [],
                                                  "role": "ROLE_CLIENTE"
                                                }
                                              ],
                                              "totalElements": 1,
                                              "totalPages": 1,
                                              "size": 10,
                                              "number": 0
                                            }
                                            """
                            )
                    )
            )
    })
    ResponseEntity<Page<UserResponseDTO>> searchByNome(
            @Parameter(description = "Trecho do nome usado na busca", example = "vinicius")
            @RequestParam(required = false) String nome,
            @PageableDefault(size = 10) Pageable pageable
    );

    @Operation(
            summary = "Atualizar usuário",
            description = "Atualiza nome, email, login e role de um usuário existente. Requer usuário autenticado com perfil cliente ou dono de restaurante."
    )
    @SecurityRequirement(name = "bearer-key")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuário atualizado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Usuário atualizado",
                                    value = """
                                            {
                                              "id": "7f9f0f1e-7a6f-4a45-8d2e-9f9f6f3b8a11",
                                              "nome": "Vinicius Oliveira Atualizado",
                                              "email": "vini.atualizado@gastrohub.com",
                                              "login": "vinicius",
                                              "dataUltimaAlteracao": "2026-05-01T12:45:10.123",
                                              "address": [],
                                              "role": "ROLE_DONO_RESTAURANTE"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados de entrada inválidos",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    name = "Validacao",
                                    value = """
                                            {
                                              "type": "https://api.gastrohub.com/errors/VALIDATION_ERROR",
                                              "title": "Erro de validação",
                                              "status": 400,
                                              "detail": "Email inválido",
                                              "instance": "/api/v1/users/7f9f0f1e-7a6f-4a45-8d2e-9f9f6f3b8a11",
                                              "code": "VALIDATION_ERROR",
                                              "timestamp": "2026-05-01T12:34:56.789Z"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário não encontrado",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    name = "Nao encontrado",
                                    value = """
                                            {
                                              "type": "https://api.gastrohub.com/errors/NOT_FOUND",
                                              "title": "Recurso não encontrado",
                                              "status": 404,
                                              "detail": "Usuário não encontrado com id: 7f9f0f1e-7a6f-4a45-8d2e-9f9f6f3b8a11",
                                              "instance": "/api/v1/users/7f9f0f1e-7a6f-4a45-8d2e-9f9f6f3b8a11",
                                              "code": "NOT_FOUND",
                                              "timestamp": "2026-05-01T12:34:56.789Z"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Email ou login já cadastrado",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Email já cadastrado",
                                            value = """
                                                    {
                                                      "type": "https://api.gastrohub.com/errors/EMAIL_ALREADY_EXISTS",
                                                      "title": "Email já cadastrado",
                                                      "status": 409,
                                                      "detail": "Email já cadastrado: vini@gastrohub.com",
                                                      "instance": "/api/v1/users/7f9f0f1e-7a6f-4a45-8d2e-9f9f6f3b8a11",
                                                      "code": "EMAIL_ALREADY_EXISTS",
                                                      "timestamp": "2026-05-01T12:34:56.789Z"
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Login já cadastrado",
                                            value = """
                                                    {
                                                      "type": "https://api.gastrohub.com/errors/LOGIN_ALREADY_EXISTS",
                                                      "title": "Login já cadastrado",
                                                      "status": 409,
                                                      "detail": "vinicius",
                                                      "instance": "/api/v1/users/7f9f0f1e-7a6f-4a45-8d2e-9f9f6f3b8a11",
                                                      "code": "LOGIN_ALREADY_EXISTS",
                                                      "timestamp": "2026-05-01T12:34:56.789Z"
                                                    }
                                                    """
                                    )
                            }
                    )
            )
    })
    ResponseEntity<UserResponseDTO> updateUser(
            @Parameter(description = "Identificador UUID do usuário", example = "7f9f0f1e-7a6f-4a45-8d2e-9f9f6f3b8a11")
            @PathVariable UUID id,
            @Valid @RequestBody UserUpdateDTO dto
    );

    @Operation(
            summary = "Excluir usuário",
            description = "Remove um usuário existente. Requer usuário autenticado com perfil dono de restaurante."
    )
    @SecurityRequirement(name = "bearer-key")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuário excluído com sucesso"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário não encontrado",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    name = "Nao encontrado",
                                    value = """
                                            {
                                              "type": "https://api.gastrohub.com/errors/NOT_FOUND",
                                              "title": "Recurso não encontrado",
                                              "status": 404,
                                              "detail": "Usuário não encontrado com id: 7f9f0f1e-7a6f-4a45-8d2e-9f9f6f3b8a11",
                                              "instance": "/api/v1/users/7f9f0f1e-7a6f-4a45-8d2e-9f9f6f3b8a11",
                                              "code": "NOT_FOUND",
                                              "timestamp": "2026-05-01T12:34:56.789Z"
                                            }
                                            """
                            )
                    )
            )
    })
    ResponseEntity<Void> deleteUser(
            @Parameter(description = "Identificador UUID do usuário", example = "7f9f0f1e-7a6f-4a45-8d2e-9f9f6f3b8a11")
            @PathVariable UUID id
    );
}
