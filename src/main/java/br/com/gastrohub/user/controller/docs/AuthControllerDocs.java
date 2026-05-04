package br.com.gastrohub.user.controller.docs;

import br.com.gastrohub.user.dto.request.LoginRequestDTO;
import br.com.gastrohub.user.dto.request.UpdatePasswordRequest;
import br.com.gastrohub.user.dto.response.AuthResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@Tag(
        name = "Autenticação",
        description = "Login JWT e troca de senha"
)
public interface AuthControllerDocs {

    @Operation(
            summary = "Realizar login",
            description = "Autentica um usuário pelo login e senha, retornando os dados básicos do usuário e um token JWT.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Credenciais de acesso do usuário",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginRequestDTO.class),
                            examples = @ExampleObject(
                                    name = "Login",
                                    value = """
                                            {
                                              "login": "cliente01",
                                              "senha": "123456"
                                            }
                                            """
                            )
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Login realizado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Token JWT",
                                    value = """
                                            {
                                              "id": "7f9f0f1e-7a6f-4a45-8d2e-9f9f6f3b8a11",
                                              "nome": "Cliente Teste",
                                              "email": "cliente@email.com",
                                              "login": "cliente01",
                                              "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjbGllbnRlMDEifQ.fake-signature",
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
                                    name = "Validação",
                                    value = """
                                            {
                                              "type": "https://api.gastrohub.com/errors/VALIDATION_ERROR",
                                              "title": "Erro de validação",
                                              "status": 400,
                                              "detail": "Login deve ter entre 3 e 50 caracteres",
                                              "instance": "/api/v1/auth/login",
                                              "code": "VALIDATION_ERROR",
                                              "timestamp": "2026-05-01T12:34:56.789Z"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Usuário inexistente ou senha inválida",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Usuário não encontrado",
                                            value = """
                                                    {
                                                      "type": "https://api.gastrohub.com/errors/BUSINESS_RULE_VIOLATION",
                                                      "title": "Erro de regra de negócio",
                                                      "status": 422,
                                                      "detail": "Usuário não encontrado",
                                                      "instance": "/api/v1/auth/login",
                                                      "code": "BUSINESS_RULE_VIOLATION",
                                                      "timestamp": "2026-05-01T12:34:56.789Z"
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Senha inválida",
                                            value = """
                                                    {
                                                      "type": "https://api.gastrohub.com/errors/BUSINESS_RULE_VIOLATION",
                                                      "title": "Erro de regra de negócio",
                                                      "status": 422,
                                                      "detail": "Senha inválida",
                                                      "instance": "/api/v1/auth/login",
                                                      "code": "BUSINESS_RULE_VIOLATION",
                                                      "timestamp": "2026-05-01T12:34:56.789Z"
                                                    }
                                                    """
                                    )
                            }
                    )
            )
    })
    ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO);

    @Operation(
            summary = "Atualizar senha do usuario",
            description = "Atualiza a senha do usuario informado pelo ID. O endpoint recebe apenas a nova senha no campo password e atualiza a data da ultima alteracao.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Nova senha do usuario",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UpdatePasswordRequest.class),
                            examples = @ExampleObject(
                                    name = "Nova senha",
                                    value = """
                                            {
                                              "password": "novaSenha123"
                                            }
                                            """
                            )
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Senha atualizada com sucesso",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario nao encontrado",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    name = "Usuario nao encontrado",
                                    value = """
                                            {
                                              "type": "https://api.gastrohub.com/errors/NOT_FOUND",
                                              "title": "Recurso nao encontrado",
                                              "status": 404,
                                              "detail": "Usuario nao encontrado com id: 7f9f0f1e-7a6f-4a45-8d2e-9f9f6f3b8a11",
                                              "instance": "/api/v1/auth/password/7f9f0f1e-7a6f-4a45-8d2e-9f9f6f3b8a11",
                                              "code": "NOT_FOUND",
                                              "timestamp": "2026-05-01T12:34:56.789Z"
                                            }
                                            """
                            )
                    )
            )
    })
    ResponseEntity<Void> updateUserPassword(
            @Valid @RequestBody UpdatePasswordRequest password,
            @Parameter(
                    description = "Identificador unico do usuario que tera a senha atualizada",
                    example = "7f9f0f1e-7a6f-4a45-8d2e-9f9f6f3b8a11"
            )
            @PathVariable UUID userID
    );
}
