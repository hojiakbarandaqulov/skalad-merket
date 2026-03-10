package org.example.service.impl;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.kafka.UserRegisteredEvent;
import org.example.dto.kafka.UserVerifiedEvent;
import org.example.service.KafkaProducerService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class KafkaProducerServiceImpl implements KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public static final String USER_REGISTERED = "user.registered";
    public static final String USER_VERIFIED = "user.verified";

    @Override
    public void sendUserRegistered(UserRegisteredEvent event) {
        kafkaTemplate.send(USER_REGISTERED,event.getUserId().toString(), event);
        log.info("Kafka -> User registered yuborildi: {}", event.getUserId());
    }

    @Override
    public void sendUserVerified(Long userId) {
        kafkaTemplate.send(USER_VERIFIED,userId.toString(),new UserVerifiedEvent(userId));
        log.info("Kafka -> User verified yuborildi: {}", userId);
    }
}
