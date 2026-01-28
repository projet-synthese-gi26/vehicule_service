package com.yowyob.template.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI customOpenAPI() {
        
        // 1. Serveur Local (PRIORITAIRE pour le dev)
        Server localServer = new Server()
                .url("http://localhost:8080")
                .description("Serveur Local (HTTP) - DEV");

        // 2. Serveur de Production
        Server prodServer = new Server()
                .url("https://vehicule-service.pynfi.com")
                .description("Serveur de Production (HTTPS)");

        return new OpenAPI()
                // <-- L'ordre est important : Local en premier par défaut
                .servers(List.of(localServer, prodServer)) 
                .info(new Info()
                        .title("YowYob Vehicle Service API")
                        .version("1.0.0")
                        .description("API de gestion des véhicules.")
                        .contact(new Contact().name("Équipe Backend").email("dev@yowyob.com"))
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME, new SecurityScheme()
                                .name(SECURITY_SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Entrez votre token JWT.")));
    }
}