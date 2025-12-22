package com.yowyob.template.infrastructure.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    // Cette ligne va lire la valeur depuis ton application.yml
    @Value("${application.kafka.topics.product-events}")
    private String productEventsTopicName;

    @Bean
    public NewTopic productEventsTopic() {
        // Cela dit à Spring de créer ce topic au démarrage s'il n'existe pas
        return TopicBuilder.name(productEventsTopicName)
                .partitions(1) // 1 partition est parfaite pour le dev local
                .replicas(1) // 1 réplique est obligatoire pour un broker seul
                .build();
    }
}