package com.yowyob.template.infrastructure.config;

import com.yowyob.template.infrastructure.adapters.outbound.external.client.StockApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class WebClientConfig {

    @Value("${application.external.stock-service-url}")
    private String stockServiceUrl;

    @Bean
    public WebClient.Builder webClientBuilder() {
        // Augmenter la taille du buffer mémoire à 16MB (par défaut 256KB)
        final int size = 16 * 1024 * 1024;
        final ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(size))
                .build();

        return WebClient.builder()
                .exchangeStrategies(strategies);
    }

    @Bean
    public StockApiClient stockApiClient(WebClient.Builder builder) {
        // 1. Création du WebClient dédié avec l'URL de base
        WebClient client = builder
                .baseUrl(stockServiceUrl)
                .build();

        // 2. Création de l'adaptateur et de la factory (Spring Boot 3.2+)
        WebClientAdapter adapter = WebClientAdapter.create(client);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

        // 3. Génération du proxy pour l'interface
        return factory.createClient(StockApiClient.class);
    }
}