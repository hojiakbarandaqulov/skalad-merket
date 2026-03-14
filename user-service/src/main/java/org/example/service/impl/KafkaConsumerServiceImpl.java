package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.kafka.UserRegisteredEvent;
import org.example.dto.kafka.UserVerifiedEvent;
import org.example.entity.Profile;
import org.example.enums.GeneralStatus;
import org.example.repository.ProfileRepository;
import org.example.service.KafkaConsumerService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumerServiceImpl implements KafkaConsumerService {

    private final ProfileRepository profileRepository;


    @KafkaListener(
            topics = "user.registered",
            groupId = "user-service-group",
            properties = {"spring.json.value.default.type=org.example.dto.kafka.UserRegisteredEvent"}
    )
    @Override
    public void onUserRegistered(UserRegisteredEvent event) {
        log.info("Kafka ← user.registered keldi, userId={}",
                event.getUserId());

        if (profileRepository.existsByUserId(event.getUserId())) {
            log.warn("Profil allaqachon bor: {}", event.getUserId());
            return;
        }
        Optional<Profile> profile = profileRepository.findByUsernameAndDeletedFalse(event.getUsername());
        profile.ifPresent(profileRepository::delete);

        Profile profileMap = Profile.builder()
                .userId(event.getUserId())
                .username(event.getUsername())
                .firstName(event.getFirstName())
                .lastName(event.getLastName())
                .password(event.getPassword())
                .roles(event.getRoles())
                .status(event.getStatus())
                .build();

        profileRepository.save(profileMap);
        log.info("✓ Profil yaratildi, userId={}", event.getUserId());
    }

    @KafkaListener(
            topics = "user.verified",
            groupId = "user-service-group",
            properties = {"spring.json.value.default.type=org.example.dto.kafka.UserVerifiedEvent"}
    )
    public void onUserVerified(UserVerifiedEvent event) {
        log.info("Kafka ← user.verified keldi, userId={}",
                event.getUserId());
        Optional<Profile> profile = profileRepository.findByUserId(event.getUserId());
        if (profile.isPresent()) {
            Profile verifiedProfile = profile.get();
            verifiedProfile.setStatus(GeneralStatus.ACTIVE);
            profileRepository.save(verifiedProfile);
        }
    }
}
