package com.yowyob.template.infrastructure.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * Filtre WebFlux pour l'authentification JWT.
 * Intercepte les requêtes, extrait le token et vérifie l'authentification
 * auprès du service externe.
 * 
 * Note: Ne pas ajouter @Component car le filtre est enregistré manuellement
 * dans SecurityConfig via addFilterAt().
 */
public class JwtAuthenticationFilter implements WebFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private static final String BEARER_PREFIX = "Bearer ";

    private final AuthServiceClient authServiceClient;

    public JwtAuthenticationFilter(AuthServiceClient authServiceClient) {
        this.authServiceClient = authServiceClient;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String token = extractToken(exchange.getRequest());
        
        // Si pas de token, laisser passer (Spring Security décidera si l'endpoint est protégé)
        if (token == null) {
            log.debug("Pas de token trouvé, laisse passer la requête: {}", exchange.getRequest().getPath());
            return chain.filter(exchange);
        }

        log.debug("Token trouvé, vérification auprès du service d'auth pour: {}", exchange.getRequest().getPath());

        // Token présent -> vérifier auprès du service d'auth AVANT de continuer
        return authServiceClient.verifyToken(token)
                .flatMap(authenticatedUser -> {
                    // Token valide -> continuer avec l'authentification
                    log.debug("Token valide pour l'utilisateur: {}", authenticatedUser.getId());
                    JwtAuthenticationToken authentication = new JwtAuthenticationToken(
                            authenticatedUser,
                            token,
                            authenticatedUser.getAuthorities()
                    );
                    
                    return chain.filter(exchange)
                            .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
                })
                // Si token invalide/expiré (Mono.empty() retourné par authServiceClient)
                // -> BLOQUER immédiatement avec 401
                .switchIfEmpty(handleUnauthorized(exchange));
    }
    
    /**
     * Gère le cas d'un token invalide ou expiré en retournant une réponse 401.
     * Cette méthode retourne un Mono<Void> qui termine la réponse immédiatement.
     */
    private Mono<Void> handleUnauthorized(ServerWebExchange exchange) {
        log.warn("Token invalide ou expiré pour la requête: {}", exchange.getRequest().getPath());
        
        ServerHttpResponse response = exchange.getResponse();
        
        // Vérifier si la réponse n'est pas déjà committed
        if (response.isCommitted()) {
            log.error("La réponse est déjà committed, impossible de renvoyer 401");
            return Mono.empty();
        }
        
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        
        String errorBody = "{\"error\":\"Unauthorized\",\"message\":\"Token invalide ou expiré\",\"status\":401}";
        byte[] bytes = errorBody.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        
        return response.writeWith(Mono.just(buffer));
    }

    private String extractToken(ServerHttpRequest request) {
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        
        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            return authHeader.substring(BEARER_PREFIX.length());
        }
        
        return null;
    }
}
