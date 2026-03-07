package org.example.service.impl;

import io.jsonwebtoken.JwtException;
import lombok.AllArgsConstructor;
import org.example.dto.ApiResponse;
import org.example.dto.RegistrationDTO;
import org.example.dto.auth.LoginDTO;
import org.example.dto.auth.ProfileDTO;
import org.example.dto.auth.ResetPasswordDTO;
import org.example.dto.auth.UpdatePasswordDTO;
import org.example.entity.Users;
import org.example.enums.AppLanguage;
import org.example.enums.GeneralStatus;
import org.example.enums.Roles;
import org.example.exp.AppBadException;
import org.example.repository.UserRepository;
import org.example.service.AuthService;
import org.example.service.EmailSendingService;
import org.example.service.ResourceBundleService;
import org.example.utils.EmailUtil;
import org.example.utils.JwtUtil;
import org.example.utils.PhoneUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final ResourceBundleService messageService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EmailSendingService emailSendingService;

    public AuthServiceImpl(UserRepository userRepository, ResourceBundleService messageService, BCryptPasswordEncoder bCryptPasswordEncoder, EmailSendingService emailSendingService) {
        this.userRepository = userRepository;
        this.messageService = messageService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.emailSendingService = emailSendingService;
    }

    @Override
    public ApiResponse<String> registration(RegistrationDTO dto, AppLanguage language) {
        Optional<Users> optional = userRepository.findByUsernameAndDeletedFalse(dto.getUsername());
        if (optional.isPresent()) {
            Users users = optional.get();
            if (users.getStatus().equals(GeneralStatus.IN_REGISTRATION)) {
                userRepository.delete(users);
            } else {
                throw new AppBadException(messageService.getMessage("email.phone.exists", language));
            }
        }
        Users entity = new Users();
        entity.setFullName(dto.getFullName());
        entity.setUsername(dto.getUsername());
        entity.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        entity.setStatus(GeneralStatus.IN_REGISTRATION);
        entity.setRole(Roles.ROLE_USER);
        userRepository.save(entity);

        if (EmailUtil.isEmail(dto.getUsername())) {
            emailSendingService.sendRegistrationEmail(dto.getUsername(), entity.getId());
        } else if (PhoneUtil.isPhone(dto.getUsername())) {
            // phone sending sms
        }
        return new ApiResponse<>(messageService.getMessage("registration.successful", language));
    }

    @Override
    public ApiResponse<String> regVerification(String token, AppLanguage lang) {
        try {
            Long profileId = JwtUtil.decodeVerRegToken(token);
            Users profile = userRepository.getReferenceById(profileId);
            if (profile.getStatus().equals(GeneralStatus.IN_REGISTRATION)) {
                userRepository.changeStatus(profileId, GeneralStatus.ACTIVE);
                return new ApiResponse<>(messageService.getMessage("verification.successful", lang));
            }
        } catch (JwtException e) {
            throw new RuntimeException(e);
        }
        throw new AppBadException(messageService.getMessage("verification.wrong", lang));
    }

    @Override
    public ApiResponse<ProfileDTO> login(LoginDTO dto, AppLanguage language) {
        Optional<Users> optional = userRepository.findByUsernameAndDeletedFalse(dto.getUsername());
        if (optional.isEmpty()) {
            throw new AppBadException(messageService.getMessage("username.password.wrong", language));
        }
        Users profile = optional.get();
        if (!bCryptPasswordEncoder.matches(dto.getPassword(), profile.getPassword())) {
            throw new AppBadException(messageService.getMessage("wrong.password", language));
        }
        if (!profile.getStatus().equals(GeneralStatus.ACTIVE)) {
            throw new AppBadException(messageService.getMessage("wrong.status", language));
        }
        ProfileDTO response = new ProfileDTO();
        response.setFullName(profile.getFullName());
        response.setUsername(profile.getUsername());
        response.setRole(profile.getRole());
        response.setJwt(JwtUtil.encode(profile.getId(), profile.getUsername(), response.getRole()));
//        response.setPhoto(attachService.attachDTO(profile.getPhotoId()));
        return new ApiResponse<>(response);
    }



    @Override
    public ApiResponse<String> resetPassword(ResetPasswordDTO dto, AppLanguage language) {
        Optional<Users> optional = userRepository.findByUsernameAndDeletedFalse(dto.getUsername());
        if (optional.isEmpty()) {
            throw new AppBadException(messageService.getMessage("username.password.wrong", language));
        }
        Users profile = optional.get();
        if (!profile.getStatus().equals(GeneralStatus.ACTIVE)) {
            throw new AppBadException(messageService.getMessage("wrong.status", language));
        }
        emailSendingService.sentResetPasswordEmail(dto.getUsername(), language);
        return new ApiResponse<>(messageService.getMessage("reset.password.response", language));
    }

    @Override
    public ApiResponse<String> resetPasswordConfirm(UpdatePasswordDTO dto, AppLanguage language) {
        Optional<Users> optional = userRepository.findByUsernameAndDeletedFalse(dto.getUsername());
        if (optional.isEmpty()) {
            throw new AppBadException(messageService.getMessage("verification.wrong", language));
        }
        Users profile = optional.get();
        if (!profile.getStatus().equals(GeneralStatus.ACTIVE)) {
            throw new AppBadException(messageService.getMessage("wrong.status", language));
        }
       /* if (PhoneUtil.isPhone(dto.getUsername())) {
//            smsService.sendSms(dto.getUsername());
        } else if (EmailUtil.isEmail(dto.getUsername())) {
            emailSendingService.sentResetPasswordEmail(dto.getUsername(), language);
        }*/
        userRepository.updatePassword(profile.getId(), bCryptPasswordEncoder.encode(dto.getNewPassword()));
        return new ApiResponse<>(messageService.getMessage("reset.password.success", language));
    }
}

