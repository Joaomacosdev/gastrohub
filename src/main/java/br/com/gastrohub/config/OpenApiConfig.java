package br.com.gastrohub.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("GastroHub API")
                        .version("v1")
                        .description("Documentação inicial da API do projeto GastroHub para o Tech Challenge - Fase 1.")
                        .contact(new Contact().name("Equipe GastroHub"))
                        .license(new License().name("Uso acadêmico")));
    }
}