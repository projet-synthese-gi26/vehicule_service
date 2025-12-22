package com.yowyob.template.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.data.domain.ReactiveAuditorAware;
import reactor.core.publisher.Mono;

@Configuration
@EnableR2dbcAuditing
public class R2dbcAuditingConfig {

    @Bean
    public ReactiveAuditorAware<String> auditorAware() {
        // Pour l'instant, on retourne "system" comme auditeur
        // Plus tard, vous pouvez intégrer Spring Security pour retourner l'utilisateur connecté
        return () -> Mono.just("system");
    }
}
