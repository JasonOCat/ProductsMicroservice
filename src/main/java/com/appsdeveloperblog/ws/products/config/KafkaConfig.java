package com.appsdeveloperblog.ws.products.config;

import com.appsdeveloperblog.ws.products.service.ProductCreatedEvent;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    private final KafkaProducerProperties kafkaProducerProperties;

    public KafkaConfig(KafkaProducerProperties kafkaProducerProperties) {
        this.kafkaProducerProperties = kafkaProducerProperties;
    }

    Map<String, Object> producerConfigs() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProducerProperties.getBootstrapServers());
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, kafkaProducerProperties.getKeySerializer());
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, kafkaProducerProperties.getValueSerializer());
        config.put(ProducerConfig.ACKS_CONFIG, kafkaProducerProperties.getAcks());
        config.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, kafkaProducerProperties.getProperties().getDeliveryTimeoutMs());
        config.put(ProducerConfig.LINGER_MS_CONFIG, kafkaProducerProperties.getProperties().getLingerMs());
        config.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, kafkaProducerProperties.getProperties().getRequestTimeoutMs());
        config.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, kafkaProducerProperties.getProperties().isIdempotence());
        config.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, kafkaProducerProperties.getProperties().getMaxInFlightRequestsPerConnection());
//        config.put(ProducerConfig.RETRIES_CONFIG, Integer.MAX_VALUE);

        return config;
    }

    @Bean
    ProducerFactory<String, ProductCreatedEvent> productProducerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    KafkaTemplate<String, ProductCreatedEvent> kafkaProductTemplate() {
        return new KafkaTemplate<>(productProducerFactory());
    }

    @Bean
    NewTopic createTopic() {
        return TopicBuilder.name("product-created-events-topic")
                .partitions(3)
                .replicas(3)
                .configs(Map.of("min.insync.replicas", "2"))
                .build();
    }
}
