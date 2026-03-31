package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.ApiResponse;
import org.example.dto.users.UsersDTO;
import org.example.dto.users.UsersResponse;
import org.example.dto.users.UsersUpdatePhoto;
import org.example.dto.users.UsersUpdateRequestDTO;
import org.example.enums.AppLanguage;
import org.example.enums.GeneralStatus;
import org.example.enums.Roles;
import org.example.service.UsersService;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public ApiResponse<String> updateProfilePhoto(@RequestBody @Valid UsersUpdatePhoto photoId) {
        return userService.updatePhoto(photoId);
    }

    @GetMapping("photo")
    public ApiResponse<UsersUpdatePhoto> getProfilePhoto() {
        return userService.getProfilePhoto();
    }

}
