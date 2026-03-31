package org.example.service;

import org.example.dto.kafka.SuperAdminSendKeycloakId;
import org.example.dto.kafka.UserRegisteredEvent;
import org.example.dto.kafka.UserRoleUpdateEvent;
import org.example.dto.kafka.UserVerifiedEvent;
import org.example.enums.Roles;

import java.util.Map;

public interface KafkaConsumerService {

    void onUserRegistered(UserRegisteredEvent event);

    void onUserVerified(UserVerifiedEvent event);

    void onUserRoleUpdate(UserRoleUpdateEvent event);

    void onKeycloakId(SuperAdminSendKeycloakId event);
}
