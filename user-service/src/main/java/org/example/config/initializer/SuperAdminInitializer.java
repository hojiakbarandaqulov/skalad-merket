/*
package org.example.config.initializer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.Profile;
import org.example.repository.ProfileRepository;
import org.example.service.KeycloakService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class SuperAdminInitializer implements ApplicationRunner {

    private final ProfileRepository profileRepository;
    private final KeycloakService keycloakService;

    @Override
    public void run(ApplicationArguments args) {
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
                    "12345"
            );

            users.setKeycloakId(keycloakId);
            profileRepository.save(users);

            keycloakService.addProfileIdAttribute(
                    keycloakId,
                    users.getId(),
                    users.getFirstName(),
                    users.getLastName(),
                    users.getUsername()
            );

            log.info("Super admin Keycloak ga muvaffaqiyatli qo'shildi");
        } catch (Exception e) {
            log.error("Super admin Keycloak ga qo'shishda xato: {}", e.getMessage());
        }
    }
}
*/
