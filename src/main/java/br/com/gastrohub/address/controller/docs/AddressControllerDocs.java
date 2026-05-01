package br.com.gastrohub.address.controller.docs;

import br.com.gastrohub.address.dto.request.AddressRequestDTO;
import br.com.gastrohub.address.dto.request.AddressUpdateDTO;
import br.com.gastrohub.address.dto.response.AddressResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@Tag(
        name = "Endereços",
        description = "Gestão de endereços vinculados a usuários autenticados"
)
@SecurityRequirement(name = "bearer-key")
public interface AddressControllerDocs {

    @Operation(
            summary = "Cadastrar endereço",
            description = "Cria um endereço e vincula ao usuário autenticado por meio do ID presente em Authentication.getDetails(), carregado a partir do token JWT."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Endereço cadastrado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AddressResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Endereço criado",
                                    value = """
                                            {
                                              "id": "b9d13e5d-6b0d-4b8f-8d8d-52d92a8848ab",
                                              "cep": "01310-100",
                                              "rua": "Avenida Paulista",
                                              "numero": "1000",
                                              "cidade": "Sao Paulo",
                                              "estado": "SP"
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
                                              "detail": "CEP deve estar no formato 00000-000",
                                              "instance": "/api/v1/address",
                                              "code": "VALIDATION_ERROR",
                                              "timestamp": "2026-05-01T12:34:56.789Z"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário autenticado não encontrado",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    name = "Usuário não encontrado",
                                    value = """
                                            {
                                              "type": "https://api.gastrohub.com/errors/NOT_FOUND",
                                              "title": "Recurso não encontrado",
                                              "status": 404,
                                              "detail": "Usuário não encontrado com id: 7f9f0f1e-7a6f-4a45-8d2e-9f9f6f3b8a11",
                                              "instance": "/api/v1/address",
                                              "code": "NOT_FOUND",
                                              "timestamp": "2026-05-01T12:34:56.789Z"
                                            }
                                            """
                            )
                    )
            )
    })
    ResponseEntity<AddressResponseDTO> createAddress(
            @Valid @RequestBody AddressRequestDTO dto,
            Authentication authentication,
            UriComponentsBuilder uriBuilder
    );

    @Operation(
            summary = "Buscar endereço por ID",
            description = "Retorna um endereço pelo identificador UUID."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Endereço encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AddressResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Endereço encontrado",
                                    value = """
                                            {
                                              "id": "b9d13e5d-6b0d-4b8f-8d8d-52d92a8848ab",
                                              "cep": "01310-100",
                                              "rua": "Avenida Paulista",
                                              "numero": "1000",
                                              "cidade": "Sao Paulo",
                                              "estado": "SP"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Endereço não encontrado",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    name = "Não encontrado",
                                    value = """
                                            {
                                              "type": "https://api.gastrohub.com/errors/NOT_FOUND",
                                              "title": "Recurso não encontrado",
                                              "status": 404,
                                              "detail": "Endereço não encontrado com id: b9d13e5d-6b0d-4b8f-8d8d-52d92a8848ab",
                                              "instance": "/api/v1/address/b9d13e5d-6b0d-4b8f-8d8d-52d92a8848ab",
                                              "code": "NOT_FOUND",
                                              "timestamp": "2026-05-01T12:34:56.789Z"
                                            }
                                            """
                            )
                    )
            )
    })
    ResponseEntity<AddressResponseDTO> findById(
            @Parameter(description = "Identificador UUID do endereço", example = "b9d13e5d-6b0d-4b8f-8d8d-52d92a8848ab")
            @PathVariable UUID id
    );

    @Operation(
            summary = "Listar endereços",
            description = "Lista todos os endereços cadastrados."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Endereços retornados com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = AddressResponseDTO.class)),
                            examples = @ExampleObject(
                                    name = "Lista de endereços",
                                    value = """
                                            [
                                              {
                                                "id": "b9d13e5d-6b0d-4b8f-8d8d-52d92a8848ab",
                                                "cep": "01310-100",
                                                "rua": "Avenida Paulista",
                                                "numero": "1000",
                                                "cidade": "Sao Paulo",
                                                "estado": "SP"
                                              }
                                            ]
                                            """
                            )
                    )
            )
    })
    ResponseEntity<List<AddressResponseDTO>> findAll();

    @Operation(
            summary = "Atualizar endereço",
            description = "Atualiza os dados de um endereço existente."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Endereço atualizado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AddressResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Endereço atualizado",
                                    value = """
                                            {
                                              "id": "b9d13e5d-6b0d-4b8f-8d8d-52d92a8848ab",
                                              "cep": "01311-000",
                                              "rua": "Rua Atualizada",
                                              "numero": "200",
                                              "cidade": "Sao Paulo",
                                              "estado": "SP"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Endereço não encontrado",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    name = "Não encontrado",
                                    value = """
                                            {
                                              "type": "https://api.gastrohub.com/errors/NOT_FOUND",
                                              "title": "Recurso não encontrado",
                                              "status": 404,
                                              "detail": "Endereço não encontrado com id: b9d13e5d-6b0d-4b8f-8d8d-52d92a8848ab",
                                              "instance": "/api/v1/address/b9d13e5d-6b0d-4b8f-8d8d-52d92a8848ab",
                                              "code": "NOT_FOUND",
                                              "timestamp": "2026-05-01T12:34:56.789Z"
                                            }
                                            """
                            )
                    )
            )
    })
    ResponseEntity<AddressResponseDTO> updateAddress(
            @Parameter(description = "Identificador UUID do endereço", example = "b9d13e5d-6b0d-4b8f-8d8d-52d92a8848ab")
            @PathVariable UUID id,
            @Valid @RequestBody AddressUpdateDTO dto
    );

    @Operation(
            summary = "Excluir endereço",
            description = "Remove um endereço existente."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Endereço excluído com sucesso"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Endereço não encontrado",
                    content = @Content(
                            mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    name = "Não encontrado",
                                    value = """
                                            {
                                              "type": "https://api.gastrohub.com/errors/NOT_FOUND",
                                              "title": "Recurso não encontrado",
                                              "status": 404,
                                              "detail": "Endereço não encontrado com id: b9d13e5d-6b0d-4b8f-8d8d-52d92a8848ab",
                                              "instance": "/api/v1/address/b9d13e5d-6b0d-4b8f-8d8d-52d92a8848ab",
                                              "code": "NOT_FOUND",
                                              "timestamp": "2026-05-01T12:34:56.789Z"
                                            }
                                            """
                            )
                    )
            )
    })
    ResponseEntity<Void> deleteAddress(
            @Parameter(description = "Identificador UUID do endereço", example = "b9d13e5d-6b0d-4b8f-8d8d-52d92a8848ab")
            @PathVariable UUID id
    );
}
