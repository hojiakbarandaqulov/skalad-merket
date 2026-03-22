package org.example.service;

import org.example.event.ProductCreatedEvent;

public interface KafkaProducerService {
    void sendProductCreated(ProductCreatedEvent event);
}
