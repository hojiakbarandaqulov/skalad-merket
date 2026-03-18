package org.example.service;

import org.example.dto.kafka.UserRoleUpdateEvent;

public interface KafkaConsumerService {
    void onUserRoleUpdate(UserRoleUpdateEvent event);
}
