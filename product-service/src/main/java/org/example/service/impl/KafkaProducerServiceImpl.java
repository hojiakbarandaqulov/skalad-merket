package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.event.ProductCreatedEvent;
import org.example.service.KafkaProducerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerServiceImpl implements KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${app.kafka.topic.product-created}")
    private String productCreatedTopic;

    @Override
    public void sendProductCreated(ProductCreatedEvent event) {
        kafkaTemplate.send(productCreatedTopic, String.valueOf(event.productId()), event);
    }
}
