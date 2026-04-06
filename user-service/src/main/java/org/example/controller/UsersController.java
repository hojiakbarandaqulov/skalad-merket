package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.ApiResponse;
import org.example.dto.users.UsersDTO;
import org.example.dto.users.UsersUpdatePhoto;
import org.example.dto.users.UsersUpdateRequestDTO;
import org.example.enums.AppLanguage;
import org.example.service.UsersService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/users")
public class UsersController {
    private final UsersService userService;

    @GetMapping
    public ApiResponse<UsersDTO> getProfile(@RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language) {
        return userService.getProfile(language);
    }

    @PutMapping
    public ApiResponse<UsersUpdateRequestDTO> updateProfile(@RequestBody @Valid UsersUpdateRequestDTO profileDTO,
                                                            @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language) {
        return userService.updateProfile(profileDTO, language);
    }

    @PutMapping("update/photo")
    public ApiResponse<String> updateProfilePhoto(@RequestBody @Valid UsersUpdatePhoto photoId,
                                                  @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language) {
        return userService.updatePhoto(photoId, language);
    }

    @GetMapping("photo")
    public ApiResponse<UsersUpdatePhoto> getProfilePhoto(@RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language) {
        return userService.getProfilePhoto(language);
    }
}
