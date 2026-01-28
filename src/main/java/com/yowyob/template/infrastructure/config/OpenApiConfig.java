package com.yowyob.template.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server; // <-- Import ajouté
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List; // <-- Import ajouté

@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI customOpenAPI() {
        
        // 1. Définition du serveur de Production (Force le HTTPS)
        Server prodServer = new Server()
                .url("https://vehicule-service.pynfi.com")
                .description("Serveur de Production (HTTPS)");

        // 2. Définition du serveur local pour le développement (Optionnel)
        Server localServer = new Server()
                .url("http://localhost:8080")
                .description("Serveur Local (HTTP)");

        return new OpenAPI()
                // <-- AJOUT ICI : On dit à Swagger d'utiliser ces URLs de base
                .servers(List.of(prodServer, localServer)) 
                .info(new Info()
                        .title("YowYob Vehicle Service API")
                        .version("1.0.0")
                        .description("API de gestion des véhicules. Toutes les routes sont protégées par authentification Bearer Token. " +
                                "Pour obtenir un token, authentifiez-vous sur le service d'authentification: https://auth-service.pynfi.com")
                        .contact(new Contact()
                                .name("Équipe Backend")
                                .email("dev@yowyob.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")))
                // Ajouter le schéma de sécurité globalement
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME, new SecurityScheme()
                                .name(SECURITY_SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Entrez votre token JWT obtenu depuis le service d'authentification. " +
                                        "Format: votre_token_jwt (sans le préfixe 'Bearer ')")));
    }
}