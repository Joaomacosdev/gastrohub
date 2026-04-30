package br.com.gastrohub;

import br.com.gastrohub.address.repository.AddressRepository;
import br.com.gastrohub.notification.service.EmailService;
import br.com.gastrohub.user.entity.User;
import br.com.gastrohub.user.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @BeforeEach
    void cleanDatabase() {
        addressRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void pingShouldReturnOk() throws Exception {
        mockMvc.perform(get("/ping"))
                .andExpect(status().isOk())
                .andExpect(content().string("pong"));
    }

    @Test
    void shouldCreateLoginListSearchUpdateAndFindUser() throws Exception {
        JsonNode createdUser = createUser(
                "Cliente Teste",
                "cliente@email.com",
                "cliente01",
                "123456",
                "ROLE_CLIENTE"
        );

        String userId = createdUser.get("id").asText();

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "login": "cliente01",
                                  "senha": "123456"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.nome").value("Cliente Teste"))
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.role").value("ROLE_CLIENTE"));

        mockMvc.perform(get("/api/v1/users")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "nome"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id").value(userId));

        mockMvc.perform(get("/api/v1/users/search")
                        .param("nome", "cliente")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].email").value("cliente@email.com"));

        mockMvc.perform(get("/api/v1/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.address", hasSize(1)))
                .andExpect(jsonPath("$.address[0].cep").value("01001-000"));

        String token = loginAndGetToken("cliente01", "123456");

        mockMvc.perform(put("/api/v1/users/{id}", userId)
                        .header("Authorization", bearer(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nome": "Cliente Atualizado",
                                  "email": "cliente.atualizado@email.com",
                                  "login": "cliente01",
                                  "role": "ROLE_CLIENTE"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.nome").value("Cliente Atualizado"))
                .andExpect(jsonPath("$.email").value("cliente.atualizado@email.com"));
    }

    @Test
    void shouldRejectDuplicateEmailAndInvalidLogin() throws Exception {
        createUser("Cliente Teste", "cliente@email.com", "cliente01", "123456", "ROLE_CLIENTE");

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userPayload("Outro Cliente", "cliente@email.com", "cliente02", "123456", "ROLE_CLIENTE")))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("EMAIL_ALREADY_EXISTS"))
                .andExpect(jsonPath("$.status").value(409));

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "login": "cliente01",
                                  "senha": "senha-errada"
                                }
                                """))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.code").value("BUSINESS_RULE_VIOLATION"))
                .andExpect(jsonPath("$.status").value(422));
    }

    @Test
    void shouldProtectAddressEndpointsAndAllowAuthenticatedCrud() throws Exception {
        createUser("Cliente Teste", "cliente@email.com", "cliente01", "123456", "ROLE_CLIENTE");

        mockMvc.perform(get("/api/v1/address"))
                .andExpect(status().isForbidden());

        String token = loginAndGetToken("cliente01", "123456");

        JsonNode createdAddress = createAddress(token);
        String addressId = createdAddress.get("id").asText();

        mockMvc.perform(get("/api/v1/address")
                        .header("Authorization", bearer(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(empty())))
                .andExpect(jsonPath("$[?(@.id == '%s')]".formatted(addressId)).exists());

        mockMvc.perform(get("/api/v1/address/{id}", addressId)
                        .header("Authorization", bearer(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(addressId))
                .andExpect(jsonPath("$.rua").value("Avenida Paulista"));

        mockMvc.perform(put("/api/v1/address/{id}", addressId)
                        .header("Authorization", bearer(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "cep": "01311-000",
                                  "rua": "Rua Atualizada",
                                  "numero": "200",
                                  "cidade": "Sao Paulo",
                                  "estado": "SP"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cep").value("01311-000"))
                .andExpect(jsonPath("$.rua").value("Rua Atualizada"));

        mockMvc.perform(delete("/api/v1/address/{id}", addressId)
                        .header("Authorization", bearer(token)))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/address/{id}", addressId)
                        .header("Authorization", bearer(token)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("NOT_FOUND"));
    }

    @Test
    void shouldRequireOwnerRoleToDeleteUsers() throws Exception {
        JsonNode cliente = createUser("Cliente Teste", "cliente@email.com", "cliente01", "123456", "ROLE_CLIENTE");
        createUser("Dono Restaurante", "dono@email.com", "dono01", "123456", "ROLE_DONO_RESTAURANTE");

        String clienteToken = loginAndGetToken("cliente01", "123456");
        String ownerToken = loginAndGetToken("dono01", "123456");
        String clienteId = cliente.get("id").asText();

        mockMvc.perform(delete("/api/v1/users/{id}", clienteId)
                        .header("Authorization", bearer(clienteToken)))
                .andExpect(status().isForbidden());

        mockMvc.perform(delete("/api/v1/users/{id}", clienteId)
                        .header("Authorization", bearer(ownerToken)))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/users/{id}", clienteId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("NOT_FOUND"));
    }

    private JsonNode createUser(String nome, String email, String login, String senha, String role) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userPayload(nome, email, login, senha, role)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.nome").value(nome))
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.login").value(login))
                .andExpect(jsonPath("$.role").value(role))
                .andExpect(jsonPath("$.address", hasSize(1)))
                .andReturn();

        return objectMapper.readTree(result.getResponse().getContentAsString());
    }

    private JsonNode createAddress(String token) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/address")
                        .header("Authorization", bearer(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "cep": "01310-100",
                                  "rua": "Avenida Paulista",
                                  "numero": "1000",
                                  "cidade": "Sao Paulo",
                                  "estado": "SP"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.cep").value("01310-100"))
                .andExpect(jsonPath("$.rua").value("Avenida Paulista"))
                .andReturn();

        return objectMapper.readTree(result.getResponse().getContentAsString());
    }

    private String loginAndGetToken(String login, String senha) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "login": "%s",
                                  "senha": "%s"
                                }
                                """.formatted(login, senha)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andReturn();

        return objectMapper.readTree(result.getResponse().getContentAsString()).get("token").asText();
    }

    private String userPayload(String nome, String email, String login, String senha, String role) {
        return """
                {
                  "nome": "%s",
                  "email": "%s",
                  "login": "%s",
                  "senha": "%s",
                  "role": "%s",
                  "address": [
                    {
                      "cep": "01001-000",
                      "rua": "Praca da Se",
                      "numero": "100",
                      "cidade": "Sao Paulo",
                      "estado": "SP"
                    }
                  ]
                }
                """.formatted(nome, email, login, senha, role);
    }

    private String bearer(String token) {
        return "Bearer " + token;
    }

    @TestConfiguration
    static class EmailTestConfiguration {

        @Bean
        @Primary
        EmailService emailService() {
            return new EmailService(null) {
                @Override
                public void enviarEmailVerificacao(User user) {
                    // Email delivery is outside the HTTP integration contract tested here.
                }
            };
        }
    }
}
