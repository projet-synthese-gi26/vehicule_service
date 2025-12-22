package com.yowyob.template.infrastructure.security;

import com.yowyob.template.infrastructure.security.dto.AuthUserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Client pour communiquer avec le service d'authentification externe.
 * Vérifie les tokens et récupère les informations utilisateur.
 */
@Component
public class AuthServiceClient {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceClient.class);
    
    private final WebClient webClient;

    public AuthServiceClient(
            WebClient.Builder webClientBuilder,
            @Value("${auth.service.url:https://auth-service.pynfi.com}") String authServiceUrl) {
        this.webClient = webClientBuilder
                .baseUrl(authServiceUrl)
                .build();
    }

    /**
     * Vérifie le token en appelant le endpoint /api/auth/me du service d'authentification.
     * 
     * @param token Le token Bearer (sans le préfixe "Bearer ")
     * @return Mono<AuthenticatedUser> si le token est valide, Mono.empty() sinon
     */
    public Mono<AuthenticatedUser> verifyToken(String token) {
        log.debug("Vérification du token auprès du service d'authentification");
        
        return webClient.get()
                .uri("/api/auth/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> {
                    log.warn("Token invalide ou expiré: {}", response.statusCode());
                    // Retourner une erreur spécifique pour les tokens invalides
                    return Mono.error(new InvalidTokenException("Token invalide ou expiré"));
                })
                .onStatus(HttpStatusCode::is5xxServerError, response -> {
                    log.error("Erreur du service d'authentification: {}", response.statusCode());
                    return Mono.error(new RuntimeException("Auth service unavailable"));
                })
                .bodyToMono(AuthUserResponse.class)
                .map(this::mapToAuthenticatedUser)
                .doOnSuccess(user -> {
                    if (user != null) {
                        log.debug("Token valide pour l'utilisateur: {}", user.getUsername());
                    }
                })
                .onErrorResume(InvalidTokenException.class, e -> {
                    // Pour les tokens invalides, retourner Mono.empty() (401 sera géré par le filtre)
                    log.debug("Token invalide détecté: {}", e.getMessage());
                    return Mono.empty();
                })
                .onErrorResume(e -> {
                    log.error("Erreur lors de la vérification du token: {}", e.getMessage());
                    return Mono.empty();
                });
    }
    
    /**
     * Exception interne pour les tokens invalides
     */
    private static class InvalidTokenException extends RuntimeException {
        public InvalidTokenException(String message) {
            super(message);
        }
    }

    private AuthenticatedUser mapToAuthenticatedUser(AuthUserResponse response) {
        return new AuthenticatedUser(
                response.id(),
                response.username(),
                response.email(),
                response.phone(),
                response.firstName(),
                response.lastName(),
                response.roles(),
                response.permissions()
        );
    }
}
