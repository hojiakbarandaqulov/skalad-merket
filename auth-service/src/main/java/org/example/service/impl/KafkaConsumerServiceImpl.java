package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.kafka.UserRoleUpdateEvent;
import org.example.dto.kafka.UserUpdateRole;
import org.example.dto.kafka.UserUpdateStatus;
import org.example.entity.Users;
import org.example.enums.Roles;
import org.example.repository.UserRepository;
import org.example.service.KafkaConsumerService;
import org.example.service.KeycloakService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class KafkaConsumerServiceImpl implements KafkaConsumerService {
    private final UserRepository userRepository;
    private final KeycloakService keycloakService;

    @KafkaListener(
            topics = "user.role.update",
            groupId = "auth-service-group",
            properties = {"spring.json.value.default.type=org.example.dto.kafka.UserRoleUpdateEvent"}
    )
    @Override
    public void onUserRoleUpdate(UserRoleUpdateEvent event) {
        log.info("Kafka ← user.role. update  keldi, userId ={}",
                event.getUserId());
        Optional<Users> profile = userRepository.findById(event.getUserId());
        if (profile.isPresent()) {
            Users verifiedProfile = profile.get();
            verifiedProfile.setRole(Roles.SELLER);
            userRepository.save(verifiedProfile);
            keycloakService.removeRole(verifiedProfile.getKeycloakId(), verifiedProfile.getRole());
            // 2. Keycloak da role yangilash
            if (verifiedProfile.getKeycloakId() != null) {
                keycloakService.assignRoleToUser(verifiedProfile.getKeycloakId(), Roles.SELLER);
                log.info("Keycloak da role SELLER ga yangilandi: keycloakId={}", verifiedProfile.getKeycloakId());
            } else {
                log.warn("User ning keycloakId si yo'q: userId={}", event.getUserId());
            }
        }
    }
    @KafkaListener(
            topics = "user.role.update.v2",
            groupId = "auth-service-group",
            properties = {"spring.json.value.default.type=org.example.dto.kafka.UserUpdateRole"}
    )
    @Override
    public void onUserUpdateRole(UserUpdateRole event) {
        Optional<Users> byId = userRepository.findById(event.getUserId());
        if (byId.isPresent()) {
            Users verifiedProfile = byId.get();
            verifiedProfile.setRole(event.getRoles());
            userRepository.save(verifiedProfile);
            keycloakService.removeRole(verifiedProfile.getKeycloakId(), event.getRoles());
            if (verifiedProfile.getKeycloakId() != null) {
                keycloakService.assignRoleToUser(verifiedProfile.getKeycloakId(), event.getRoles());
                log.info("Keycloak da role yangilandi: keycloakId={}", verifiedProfile.getKeycloakId());
            }else {
                log.warn("User ning keycloakId si yo'q");
            }
        }
    }

    @KafkaListener(
            topics = "user.status.update",
            groupId = "auth-service-group",
            properties = {"spring.json.value.default.type=org.example.dto.kafka.UserUpdateStatus"}
    )
    @Override
    public void onUserUpdateStatus(UserUpdateStatus event) {
        Optional<Users> byId = userRepository.findById(event.getUserId());
        if (byId.isPresent()) {
            Users verifiedProfile = byId.get();
            verifiedProfile.setStatus(event.getStatus());
            userRepository.save(verifiedProfile);
            log.info("User statusi yangilandi");
        }else {
            log.warn("User ning statusi yangilanmadi");
        }
    }
}
