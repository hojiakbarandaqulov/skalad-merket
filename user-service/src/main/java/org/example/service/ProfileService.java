package org.example.service;

import org.example.dto.ApiResponse;
import org.example.dto.profile.ProfileDTO;
import org.example.dto.profile.ProfileUpdatePhoto;
import org.example.dto.profile.ProfileUpdateRequestDTO;
import org.example.enums.AppLanguage;
import org.springframework.web.multipart.MultipartFile;

public interface ProfileService {
    ApiResponse<ProfileDTO> getProfile(AppLanguage language);

    ApiResponse<ProfileUpdateRequestDTO> updateProfile(ProfileUpdateRequestDTO profileDTO, AppLanguage language);


    ApiResponse<String> updatePhoto(ProfileUpdatePhoto photoId);

    ApiResponse<ProfileUpdatePhoto> getProfilePhoto();
}
