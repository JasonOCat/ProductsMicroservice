package com.appsdeveloperblog.ws.products.config;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "spring.kafka.producer")
public class KafkaProducerProperties {
    private String bootstrapServers;
    private String keySerializer;
    private String valueSerializer;
    private String acks;
    @NonNull
    private PropertiesProperty properties;

    @Getter
    @Setter
    public static class PropertiesProperty {
        private Integer deliveryTimeoutMs;
        private Integer lingerMs;
        private Integer requestTimeoutMs;

    }
}

