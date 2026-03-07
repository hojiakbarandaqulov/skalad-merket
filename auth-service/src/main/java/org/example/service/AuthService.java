package org.example.service;

import io.jsonwebtoken.JwtException;
import org.example.dto.ApiResponse;
import org.example.dto.RegistrationDTO;
import org.example.dto.auth.LoginDTO;
import org.example.dto.auth.ProfileDTO;
import org.example.dto.auth.ResetPasswordDTO;
import org.example.dto.auth.UpdatePasswordDTO;
import org.example.entity.Users;
import org.example.enums.AppLanguage;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
public interface AuthService {
     ApiResponse<String> registration(RegistrationDTO dto, AppLanguage language);

     ApiResponse<String> regVerification(String token, AppLanguage lang);

     ApiResponse<ProfileDTO> login(LoginDTO dto, AppLanguage language);

     ApiResponse<String> resetPassword(ResetPasswordDTO dto, AppLanguage language);

     ApiResponse<String> resetPasswordConfirm(UpdatePasswordDTO dto, AppLanguage language);
/*
     ApiResponse<String> regVerification(String token, AppLanguage language) {
        try {
            Integer profileId = JwtUtil.decodeVerRegToken(token);
            Users profile = profileService.getById(profileId);
            if (profile.getStatus().equals(GeneralStatus.IN_REGISTRATION)) {
                profileRepository.changeStatus(profileId, GeneralStatus.ACTIVE);
                return new ApiResponse<>(messagesService.getMessage("verification.successful", language));
            }
        } catch (JwtException e) {
            throw new RuntimeException(e);
        }
        throw new AppBadException(messagesService.getMessage("verification.wrong", language));
    }*/

    /* ApiResponse<ProfileDTO> login(LoginDTO loginDTO, AppLanguage language) {
        Optional<Users> optional = profileRepository.findByUsernameAndVisibleTrue(loginDTO.getUsername());
        if (optional.isEmpty()) {
            throw new AppBadException(messagesService.getMessage("username.password.wrong", language));
        }
        Users profile = optional.get();
        if (!bCryptPasswordEncoder.matches(loginDTO.getPassword(), profile.getPassword())) {
            throw new AppBadException(messagesService.getMessage("wrong.password", language));
        }
        if (!profile.getStatus().equals(GeneralStatus.ACTIVE)) {
            throw new AppBadException(messagesService.getMessage("wrong.status", language));
        }
        ProfileDTO response = new ProfileDTO();
        response.setName(profile.getName());
        response.setUsername(profile.getUsername());
        response.setRole(profileRoleRepository.getAllRolesListByProfileId(profile.getId()));
        response.setJwt(JwtUtil.encode(profile.getUsername(), profile.getId(), response.getRole()));
        response.setName(profile.getName());
        response.setPhoto(attachService.attachDTO(profile.getPhotoId()));
        return new ApiResponse<>(response);
    }*/

}
