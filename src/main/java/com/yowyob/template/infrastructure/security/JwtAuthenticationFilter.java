//---> PATH: src/main/java/com/yowyob/template/infrastructure/security/JwtAuthenticationFilter.java

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

    // Exception interne pour contrôler le flux réactif sans exposer d'erreur système
    private static class TokenInvalidException extends RuntimeException {}

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String token = extractToken(exchange.getRequest());
        
        // 1. Si pas de token, on laisse passer (la config Security décidera si c'est public ou non)
        if (token == null) {
            log.debug("Pas de token trouvé, laisse passer la requête: {}", exchange.getRequest().getPath());
            return chain.filter(exchange);
        }

        log.debug("Token trouvé, vérification auprès du service d'auth pour: {}", exchange.getRequest().getPath());

        return authServiceClient.verifyToken(token)
                // 2. Si verifyToken renvoie empty (token invalide), on lève une erreur spécifique ICI
                .switchIfEmpty(Mono.error(new TokenInvalidException()))
                
                // 3. Si on arrive ici, c'est que le token est valide (user n'est pas null)
                .flatMap(authenticatedUser -> {
                    log.debug("Token valide pour l'utilisateur: {}", authenticatedUser.getId());
                    JwtAuthenticationToken authentication = new JwtAuthenticationToken(
                            authenticatedUser,
                            token,
                            authenticatedUser.getAuthorities()
                    );
                    
                    // On injecte l'authentification et on passe la main au contrôleur
                    return chain.filter(exchange)
                            .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
                })
                
                // 4. On attrape uniquement l'erreur "TokenInvalidException" pour renvoyer le 401
                // Les autres erreurs (ex: contrôleur qui crash) ne passeront pas par ici.
                .onErrorResume(TokenInvalidException.class, e -> handleUnauthorized(exchange));
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