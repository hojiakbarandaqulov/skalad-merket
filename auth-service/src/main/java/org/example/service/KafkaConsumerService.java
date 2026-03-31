package org.example.service;

import org.example.dto.kafka.UserRoleUpdateEvent;
import org.example.dto.kafka.UserUpdateRole;
import org.example.dto.kafka.UserUpdateStatus;

public interface KafkaConsumerService {
    void onUserRoleUpdate(UserRoleUpdateEvent event);
    void onUserUpdateRole(UserUpdateRole event);
    void onUserUpdateStatus(UserUpdateStatus event);
}
