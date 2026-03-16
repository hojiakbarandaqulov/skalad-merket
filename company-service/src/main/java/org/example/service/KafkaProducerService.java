package org.example.service;

import org.example.dto.kafka.UserRoleUpdateEvent;
import org.example.enums.Roles;

public interface KafkaProducerService {

    void sendUserRoleUpdate(UserRoleUpdateEvent event);
}
