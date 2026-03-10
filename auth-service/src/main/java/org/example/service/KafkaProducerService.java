package org.example.service;

import org.example.dto.kafka.UserRegisteredEvent;

public interface KafkaProducerService {

    void sendUserRegistered(UserRegisteredEvent event);
     void sendUserVerified(Long userId);
}
