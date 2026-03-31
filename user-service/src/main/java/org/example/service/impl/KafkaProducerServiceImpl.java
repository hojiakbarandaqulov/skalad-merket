package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.kafka.UserRoleUpdateEvent;
import org.example.dto.kafka.UserUpdateRole;
import org.example.dto.kafka.UserUpdateStatus;
import org.example.service.KafkaProducerService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerServiceImpl implements KafkaProducerService {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final static String USER_ROLE_UPDATE = "user.role.update.v2";
    private final static String USER_STATUS_UPDATE = "user.status.update";

    @Override
    public void sendUserRoleUpdate(UserUpdateRole event) {
        kafkaTemplate.send(USER_ROLE_UPDATE, event);
    }

    @Override
    public void sendUserStatusUpdate(UserUpdateStatus event) {
        kafkaTemplate.send(USER_STATUS_UPDATE, event);
    }
}
