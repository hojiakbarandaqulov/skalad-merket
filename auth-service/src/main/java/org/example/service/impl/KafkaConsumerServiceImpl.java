package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.kafka.UserRoleUpdateEvent;
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

            // 2. Keycloak da role yangilash
            if (verifiedProfile.getKeycloakId() != null) {
                keycloakService.assignRoleToUser(verifiedProfile.getKeycloakId(), Roles.SELLER);
                log.info("Keycloak da role SELLER ga yangilandi: keycloakId={}", verifiedProfile.getKeycloakId());
            } else {
                log.warn("User ning keycloakId si yo'q: userId={}", event.getUserId());
            }
        }
    }
}
