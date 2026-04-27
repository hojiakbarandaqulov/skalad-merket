package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.internal.ProfileInternalSummaryResponse;
import org.example.entity.Profile;
import org.example.enums.GeneralStatus;
import org.example.exp.AppBadException;
import org.example.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/profiles")
public class ProfileInternalController {

    private final UsersRepository profileRepository;

    @Value("${aws.url}")
    private String awsUrl;

    @GetMapping("/{userId}/summary")
    public ProfileInternalSummaryResponse summary(@PathVariable Long userId) {
        Profile profile = profileRepository.findByUserIdAndDeletedFalse(userId);
        if (profile == null) {
            throw new AppBadException("profile not found");
        }

        String photoUrl = profile.getPhoto() == null ? null : awsUrl + "/" + profile.getPhoto().getPath();
        return ProfileInternalSummaryResponse.builder()
                .id(profile.getUserId())
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .username(profile.getUsername())
                .photoUrl(photoUrl)
                .build();
    }

    @PostMapping("/{userId}/warning")
    public void increaseWarning(@PathVariable Long userId) {
        Profile profile = profileRepository.findByUserIdAndDeletedFalse(userId);
        if (profile == null) {
            throw new AppBadException("profile not found");
        }
        profile.setWarningCount((profile.getWarningCount() == null ? 0 : profile.getWarningCount()) + 1);
        profileRepository.save(profile);
    }

    @GetMapping("/stats/blocked-count")
    public Map<String, Long> blockedCount() {
        return Map.of("count", profileRepository.countByStatusAndDeletedFalse(GeneralStatus.BLOCK));
    }
}
