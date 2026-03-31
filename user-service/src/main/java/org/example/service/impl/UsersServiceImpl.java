package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.ApiResponse;
import org.example.dto.kafka.UserUpdateRole;
import org.example.dto.kafka.UserUpdateStatus;
import org.example.dto.users.AdminUserDetailResponse;
import org.example.dto.users.UsersDTO;
import org.example.dto.users.UsersResponse;
import org.example.dto.users.UsersUpdatePhoto;
import org.example.dto.users.UsersUpdateRequestDTO;
import org.example.entity.Attach;
import org.example.entity.Profile;
import org.example.enums.AppLanguage;
import org.example.enums.GeneralStatus;
import org.example.enums.Roles;
import org.example.exp.AppBadException;
import org.example.repository.UsersRepository;
import org.example.service.*;
import org.example.utils.SpringSecurityUtil;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {

    private final UsersRepository profileRepository;
    private final ModelMapper modelMapper;
    private final ResourceBundleService messageService;
    private final AttachService attachService;
    protected final KeycloakService keycloakService;
    private final KafkaProducerService kafkaProducerService;
    @Override
    public ApiResponse<UsersDTO> getProfile(AppLanguage language) {
        Long profileId = SpringSecurityUtil.getProfileId();
        Profile profile = profileRepository.findByUserIdAndDeletedFalse(profileId);
        if (profile == null) {
            throw new AppBadException(messageService.getMessage("user.not.found", language));
        }
        UsersDTO map = modelMapper.map(profile, UsersDTO.class);
        return new ApiResponse<>(map);
    }

    @Override
    public ApiResponse<UsersUpdateRequestDTO> updateProfile(UsersUpdateRequestDTO profileDTO, AppLanguage language) {
        Long profileId = SpringSecurityUtil.getProfileId();
        Profile profile = profileRepository.findByUserIdAndDeletedFalse(profileId);
        if (profile == null) {
            throw new AppBadException(messageService.getMessage("user.not.found", language));
        }
        modelMapper.map(profileDTO, profile);
        profileRepository.save(profile);
        UsersUpdateRequestDTO updateRequestDTO = modelMapper.map(profile, UsersUpdateRequestDTO.class);
        return new ApiResponse<>(updateRequestDTO);
    }

    @Override
    public ApiResponse<String> updatePhoto(UsersUpdatePhoto photoId) {
        Long profileId = SpringSecurityUtil.getProfileId();
        Profile profile = profileRepository.findByUserIdAndDeletedFalse(profileId);
        if (profile.getPhoto() != null && profile.getPhoto().getId().equals(photoId.getPhotoId())) {
            attachService.delete(profile.getPhoto().getId());
        }
        Attach attach = attachService.get(photoId.getPhotoId());
        profileRepository.updatePhoto(profile.getId(), attach);
        return ApiResponse.successResponse("profile photo update success");
    }

  /*  @Override
    public ApiResponse<String> updatePhoto(ProfileUpdatePhoto photoId) {
        Long userId = SpringSecurityUtil.getProfileId();

        Profile profile = profileRepository.findByUserIdAndDeletedFalse(userId)
                .orElseThrow(() -> new AppBadException("User topilmadi"));

        if (profile.getPhoto() != null && profile.getPhoto().getId().equals(photoId.getPhotoId())) {
            attachService.delete(profile.getPhoto().getId());
        }

        Attach attach = attachService.get(photoId.getPhotoId());

        // userId emas, profile.getId() (DB dagi haqiqiy id)
        profileRepository.updatePhoto(profile.getId(), attach); // ← TO'G'RI

        return ApiResponse.successResponse("profile photo update success");
    }*/

    @Override
    public ApiResponse<UsersUpdatePhoto> getProfilePhoto() {
        Long profileId = SpringSecurityUtil.getProfileId();
        assert profileId != null;
        Profile profile = profileRepository.findByUserIdAndDeletedFalse(profileId);
        if (profile == null) {
            throw new AppBadException("profile not found");
        }
        UsersUpdatePhoto photoUpdatePhoto = new UsersUpdatePhoto();
        photoUpdatePhoto.setPhotoId(String.valueOf(profile.getPhoto().getId()));
        return ApiResponse.successResponse(photoUpdatePhoto);
    }

    @Override
    public PageImpl<UsersResponse> getUsers(String q, GeneralStatus status, Roles roles, int page, int perPage) {
        PageRequest pageRequest = PageRequest.of(page - 1, perPage, Sort.Direction.DESC, "createdDate");
        Specification<Profile> spec = Specification.where(notDeleted());
        if (q != null && !q.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.or(
                    cb.like(cb.lower(root.get("firstName")), "%" + q.toLowerCase() + "%"),
                    cb.like(cb.lower(root.get("lastName")), "%" + q.toLowerCase() + "%"),
                    cb.like(cb.lower(root.get("username")), "%" + q.toLowerCase() + "%")
            ));
        }
        if (status != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
        }
        if (roles != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("roles"), roles));
        }
        Page<Profile> result = profileRepository.findAll(spec, pageRequest);
        List<UsersResponse> items = result.getContent()
                .stream()
                .map(this::toResponse)
                .toList();
        return new PageImpl<>(items, pageRequest, result.getTotalElements());
    }


    @Override
    public ApiResponse<String> setAdmin(Long userId, Roles role) {
        Profile byUserIdAndDeletedFalse = profileRepository.findByUserIdAndDeletedFalse(userId);
        keycloakService.removeRole(byUserIdAndDeletedFalse.getKeycloakId(), byUserIdAndDeletedFalse.getRoles());
        byUserIdAndDeletedFalse.setRoles(role);
        profileRepository.save(byUserIdAndDeletedFalse);
        keycloakService.assignRoleToUser(byUserIdAndDeletedFalse.getKeycloakId(), role);
        log.info("Keycloak da role ADMIN ga yangilandi");
        kafkaProducerService.sendUserRoleUpdate(new UserUpdateRole(userId,role));
        return ApiResponse.successResponse("success");
    }

    @Override
    public ApiResponse<AdminUserDetailResponse> getUserById(Long userId) {
        Profile profile = getByUserId(userId);
        AdminUserDetailResponse response = new AdminUserDetailResponse();
        response.setId(profile.getUserId());
        response.setFirstName(profile.getFirstName());
        response.setLastName(profile.getLastName());
        response.setFullName(buildFullName(profile));
        response.setUsername(profile.getUsername());
        response.setPosition(profile.getPosition());
        response.setTelegram(profile.getTelegram());
        response.setExtraPhone(profile.getExtraPhone());
        response.setStatus(profile.getStatus());
        response.setRoles(profile.getRoles());
        response.setWarningCount(profile.getWarningCount());
        response.setCreatedDate(profile.getCreatedDate());
        response.setModifiedDate(profile.getModifiedDate());
        return ApiResponse.successResponse(response);
    }

    @Override
    public ApiResponse<String> blockUser(Long userId, String reason) {
        Profile profile = getByUserId(userId);
        profile.setStatus(GeneralStatus.BLOCK);
        profileRepository.save(profile);
        kafkaProducerService.sendUserStatusUpdate(new UserUpdateStatus(profile.getUserId(), profile.getStatus()));
        keycloakService.setUserEnabled(profile.getKeycloakId(), false);
        keycloakService.revokeUserSessions(profile.getKeycloakId());
        log.info("User blocked: userId={}, reason={}", userId, reason);
        return ApiResponse.successResponse("user blocked");
    }

    @Override
    public ApiResponse<String> unblockUser(Long userId) {
        Profile profile = getByUserId(userId);
        profile.setStatus(GeneralStatus.ACTIVE);
        profileRepository.save(profile);
        kafkaProducerService.sendUserStatusUpdate(new UserUpdateStatus(profile.getUserId(), profile.getStatus()));
        keycloakService.setUserEnabled(profile.getKeycloakId(), true);
        return ApiResponse.successResponse("user unblocked");
    }

    @Override
    public ApiResponse<String> revokeUserSessions(Long userId) {
        Profile profile = getByUserId(userId);
        keycloakService.revokeUserSessions(profile.getKeycloakId());
        return ApiResponse.successResponse("user sessions revoked");
    }

    private UsersResponse toResponse(Profile profile) {
        UsersResponse usersResponse = new UsersResponse();
        usersResponse.setId(profile.getUserId());
        usersResponse.setName(buildFullName(profile));
        usersResponse.setUsername(profile.getUsername());
        usersResponse.setStatus(profile.getStatus());
        usersResponse.setRoles(profile.getRoles());
        usersResponse.setWarningCount(profile.getWarningCount());
        usersResponse.setCreatedDate(profile.getCreatedDate());
        return usersResponse;
    }

    private Specification<Profile> notDeleted() {
        return (root, query, cb) -> cb.isFalse(root.get("deleted"));
    }


    private Profile getById(Long id) {
        return profileRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new AppBadException("Profile not found"));
    }

    private Profile getByUserId(Long userId) {
        Profile profile = profileRepository.findByUserIdAndDeletedFalse(userId);
        if (profile == null) {
            throw new AppBadException("Profile not found");
        }
        return profile;
    }

    private String buildFullName(Profile profile) {
        String firstName = profile.getFirstName() == null ? "" : profile.getFirstName().trim();
        String lastName = profile.getLastName() == null ? "" : profile.getLastName().trim();
        return (firstName + " " + lastName).trim();
    }

}
