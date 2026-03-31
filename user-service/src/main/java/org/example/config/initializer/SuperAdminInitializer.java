/*
package org.example.config.initializer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.Profile;
import org.example.repository.UsersRepository;
import org.example.service.KeycloakService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class SuperAdminInitializer{

    private final UsersRepository profileRepository;
    private final KeycloakService keycloakService;

    @EventListener(ApplicationReadyEvent.class)
    public void run() {
        Optional<Profile> superAdmin = profileRepository.findByUsernameAndDeletedFalse(
                "xojiakbarandaqulov@gmail.com"
        );
        if (superAdmin.isEmpty()) return;

        Profile users=superAdmin.get();

        if (users.getKeycloakId() != null) {
            log.info("Super admin allaqachon Keycloak da bor");
            return;
        }

        try {
            String keycloakId = keycloakService.createUser(
                    users.getFirstName(),
                    users.getLastName(),
                    users.getUsername(),
                    "super-admin",
                    users.getRoles()
            );

            users.setKeycloakId(keycloakId);
            profileRepository.save(users);

            keycloakService.addProfileIdAttribute(
                    keycloakId,
                    users.getId(),
                    users.getFirstName(),
                    users.getLastName(),
                    users.getUsername(),
                    users.getPassword()
            );

            log.info("Super admin Keycloak ga muvaffaqiyatli qo'shildi");
        } catch (Exception e) {
            log.error("Super admin Keycloak ga qo'shishda xato: {}", e.getMessage());
        }
    }
}
*/
