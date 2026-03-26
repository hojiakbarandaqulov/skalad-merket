package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.internal.ProfileInternalSummaryResponse;
import org.example.entity.Profile;
import org.example.exp.AppBadException;
import org.example.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/profiles")
public class ProfileInternalController {

    private final ProfileRepository profileRepository;

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
}
