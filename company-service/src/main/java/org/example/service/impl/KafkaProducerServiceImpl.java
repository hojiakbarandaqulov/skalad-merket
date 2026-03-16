package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.example.dto.kafka.UserRoleUpdateEvent;
import org.example.enums.Roles;
import org.example.service.KafkaProducerService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class KafkaProducerServiceImpl implements KafkaProducerService {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String USER_ROLE_UPDATE = "user.role.update";

    @Override
    public void sendUserRoleUpdate(UserRoleUpdateEvent event) {
        kafkaTemplate.send(USER_ROLE_UPDATE, event.toString(), event);
    }
}
