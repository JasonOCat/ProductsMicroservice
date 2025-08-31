package com.appsdeveloperblog.ws.products.service;

import com.appsdeveloperblog.ws.products.rest.CreateProductRestModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final KafkaTemplate<String, ProductCreatedEvent> kafkaProductTemplate;

    @Override
    public String createProduct(CreateProductRestModel productRestModel) {

        String productId = UUID.randomUUID().toString();

        // TODO: Persist product details into database table before publishing an event

        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent(
                productId,
                productRestModel.getTitle(),
                productRestModel.getPrice(),
                productRestModel.getQuantity()
        );
        kafkaProductTemplate.send("product-created-events-topic", productId, productCreatedEvent)
                .thenAccept(result -> {
                    if (result != null) {
                        RecordMetadata recordMetadata = result.getRecordMetadata();
                        log.info("****** Message sent successfully: {}", recordMetadata);
                        log.info(
                                """
                                        Partition: {},
                                        Topic: {},
                                        Offset: {}
                                        """,
                                recordMetadata.partition(),
                                recordMetadata.topic(),
                                recordMetadata.offset()
                        );


                    }
                })
                .exceptionally(exception -> {
                    log.error("****** Failed to send message: {}", exception.getMessage(), exception);
                    return null;
                });

        log.info("****** Returning product id");

        return productId;
    }
}
