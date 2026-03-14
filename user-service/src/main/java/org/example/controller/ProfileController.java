package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.ApiResponse;
import org.example.dto.profile.ProfileDTO;
import org.example.dto.profile.ProfileUpdatePhoto;
import org.example.dto.profile.ProfileUpdateRequestDTO;
import org.example.enums.AppLanguage;
import org.example.service.ProfileService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/profile")
public class ProfileController {
    private final ProfileService profileService;

    @GetMapping
    public ApiResponse<ProfileDTO> getProfile(@RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language) {
        return profileService.getProfile(language);
    }

    @PutMapping
    public ApiResponse<ProfileUpdateRequestDTO> updateProfile(@RequestBody @Valid ProfileUpdateRequestDTO profileDTO,
                                                              @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language) {
        return profileService.updateProfile(profileDTO,language);
    }

    @PutMapping("update/photo")
    public ApiResponse<String> updateProfilePhoto(@RequestBody @Valid ProfileUpdatePhoto photoId) {
        return profileService.updatePhoto(photoId);
    }

    @GetMapping("photo")
    public ApiResponse<ProfileUpdatePhoto> getProfilePhoto() {
        return profileService.getProfilePhoto();
    }

}
