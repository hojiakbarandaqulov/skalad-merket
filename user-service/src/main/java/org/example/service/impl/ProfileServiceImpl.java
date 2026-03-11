package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.ApiResponse;
import org.example.dto.AttachDTO;
import org.example.dto.profile.ProfileDTO;
import org.example.dto.profile.ProfileUpdatePhoto;
import org.example.dto.profile.ProfileUpdateRequestDTO;
import org.example.entity.Attach;
import org.example.entity.Profile;
import org.example.enums.AppLanguage;
import org.example.exp.AppBadException;
import org.example.repository.ProfileRepository;
import org.example.service.AttachService;
import org.example.service.ProfileService;
import org.example.service.ResourceBundleService;
import org.example.utils.SpringSecurityUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final ModelMapper modelMapper;
    private final ResourceBundleService messageService;
    private final AttachService attachService;

    @Override
    public ApiResponse<ProfileDTO> getProfile(AppLanguage language) {
        Long profileId = SpringSecurityUtil.getProfileId();
        Profile profile = profileRepository.findByUserIdAndDeletedFalse(profileId);
        if (profile==null) {
            throw new AppBadException(messageService.getMessage("user.not.found", language));
        }
        ProfileDTO map = modelMapper.map(profile, ProfileDTO.class);
        return new ApiResponse<>(map);
    }

    @Override
    public ApiResponse<ProfileUpdateRequestDTO> updateProfile(ProfileUpdateRequestDTO profileDTO, AppLanguage language) {
        Long profileId = SpringSecurityUtil.getProfileId();
        Profile profile = profileRepository.findByUserIdAndDeletedFalse(profileId);
        if (profile==null) {
            throw new AppBadException(messageService.getMessage("user.not.found", language));
        }
        modelMapper.map(profileDTO,profile);
        profileRepository.save(profile);
        ProfileUpdateRequestDTO updateRequestDTO = modelMapper.map(profile, ProfileUpdateRequestDTO.class);
        return new ApiResponse<>(updateRequestDTO);
    }

    @Override
    public ApiResponse<String> updatePhoto(ProfileUpdatePhoto photoId) {
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
    public ApiResponse<ProfileUpdatePhoto> getProfilePhoto() {
        Long profileId = SpringSecurityUtil.getProfileId();
        assert profileId != null;
        Profile profile = profileRepository.findByUserIdAndDeletedFalse(profileId);
        if (profile==null) {
            throw new AppBadException("profile not found");
        }
        ProfileUpdatePhoto photoUpdatePhoto = new ProfileUpdatePhoto();
        photoUpdatePhoto.setPhotoId(String.valueOf(profile.getPhoto().getId()));
        return ApiResponse.successResponse(photoUpdatePhoto);
    }


    private Profile getById(Long id) {
        return profileRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new AppBadException("Profile not found"));
    }

}
