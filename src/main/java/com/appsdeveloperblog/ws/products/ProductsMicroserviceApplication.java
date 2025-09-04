package com.appsdeveloperblog.ws.products;

import com.appsdeveloperblog.ws.products.config.KafkaProducerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ProductsMicroserviceApplication {

    @Autowired
    KafkaProducerProperties kafkaProducerProperties;

    public static void main(String[] args) {
        SpringApplication.run(ProductsMicroserviceApplication.class, args);
    }

}
