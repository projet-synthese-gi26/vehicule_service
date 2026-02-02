package com.yowyob.template.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod; // <--- Import nécessaire
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import reactor.core.publisher.Mono;

import java.util.List;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    private final AuthServiceClient authServiceClient;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(AuthServiceClient authServiceClient) {
        this.authServiceClient = authServiceClient;
        this.jwtAuthenticationFilter = new JwtAuthenticationFilter(authServiceClient);
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                // On laisse le CorsWebFilter gérer le CORS
                .cors(ServerHttpSecurity.CorsSpec::disable)
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
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
                .authorizeExchange(exchanges -> exchanges
                        // 👇 INDISPENSABLE : On autorise TOUTES les requêtes OPTIONS (CORS preflight) sans token
                        .pathMatchers(HttpMethod.OPTIONS).permitAll()
                        
                        // Tes routes publiques (Swagger, etc.)
                        .pathMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/webjars/**", "/actuator/**").permitAll()

                        // Route publique: véhicules par utilisateur
                        .pathMatchers(HttpMethod.GET, "/vehicles/user/**").permitAll()
                        
                        // Tout le reste nécessite un token
                        .anyExchange().authenticated()
                )
                .addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        // En prod, l'étoile '*' peut être bloquée par certains navigateurs si allowCredentials=true
        // Mieux vaut utiliser setAllowedOriginPatterns qui est plus souple
        corsConfig.setAllowedOriginPatterns(List.of("*")); 
        corsConfig.setMaxAge(8000L);
        corsConfig.addAllowedMethod("*");
        corsConfig.addAllowedHeader("*");
        corsConfig.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }
}