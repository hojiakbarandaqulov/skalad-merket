package org.example.service;

import org.example.dto.kafka.UserRegisteredEvent;
import org.example.dto.kafka.UserVerifiedEvent;

public interface KafkaConsumerService {

     void onUserRegistered(UserRegisteredEvent event);
    void onUserVerified(UserVerifiedEvent event);
}
