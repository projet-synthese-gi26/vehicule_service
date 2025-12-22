package com.yowyob.template.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import reactor.core.publisher.Mono;

/**
 * Configuration de la sécurité WebFlux.
 * Définit les règles d'accès aux endpoints et intègre le filtre JWT.
 */
@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    private final AuthServiceClient authServiceClient;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(AuthServiceClient authServiceClient) {
        this.authServiceClient = authServiceClient;
        // Créer une seule instance du filtre
        this.jwtAuthenticationFilter = new JwtAuthenticationFilter(authServiceClient);
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                // Désactiver CSRF car nous utilisons des tokens JWT (stateless)
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                
                // Pas de session (stateless)
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                
                // Gestion des erreurs d'authentification
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint((exchange, ex) -> {
                            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                            return Mono.empty();
                        })
                        .accessDeniedHandler((exchange, denied) -> {
                            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                            return Mono.empty();
                        })
                )
                
                // Règles d'autorisation
                .authorizeExchange(exchanges -> exchanges
                        // Endpoints publics (documentation Swagger)
                        .pathMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/webjars/**"
                        ).permitAll()
                        
                        // Endpoint de santé (si vous en avez)
                        .pathMatchers("/actuator/**").permitAll()
                        
                        // Toutes les autres requêtes nécessitent une authentification
                        .anyExchange().authenticated()
                )
                
                // Ajouter notre filtre JWT
                .addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                
                .build();
    }
}
