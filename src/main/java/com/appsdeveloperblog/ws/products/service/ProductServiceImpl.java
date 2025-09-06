package com.appsdeveloperblog.ws.products.service;

import com.appsdeveloperblog.ws.products.ProductCreatedEvent;
import com.appsdeveloperblog.ws.products.rest.CreateProductRestModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
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


        // Convert BigDecimal to ByteBuffer for Avro decimal field
//        ByteBuffer encodedPrice = logicalDecimalToBytes(productRestModel.getPrice(), 12, 2); // Precision = 12, Scale = 2


        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent(
                productId,
                productRestModel.getTitle(),
                productRestModel.getPrice(),
//                encodedPrice,
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


//    /**
//     * Encode BigDecimal to ByteBuffer as per Avro's `decimal` logical type.
//     *
//     * @param decimal  The BigDecimal to encode.
//     * @param precision Precision of the decimal field.
//     * @param scale     Scale of the decimal field (number of fractional digits).
//     * @return A ByteBuffer representing the BigDecimal.
//     */
//    private ByteBuffer logicalDecimalToBytes(BigDecimal decimal, int precision, int scale) {
//        // Ensure the scale matches the desired Avro schema scale
//        BigDecimal scaledDecimal = decimal.setScale(scale);
//
//        // Get the unscaled value as BigInteger
//        BigInteger unscaledValue = scaledDecimal.unscaledValue();
//
//        // Convert the BigInteger to a byte array
//        byte[] unscaledBytes = unscaledValue.toByteArray();
//
//        // Return as ByteBuffer
//        return ByteBuffer.wrap(unscaledBytes);
//    }

}
