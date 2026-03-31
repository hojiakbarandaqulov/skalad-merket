package org.example.service;

import org.example.dto.ApiResponse;
import org.example.dto.users.AdminUserDetailResponse;
import org.example.dto.users.UsersDTO;
import org.example.dto.users.UsersResponse;
import org.example.dto.users.UsersUpdatePhoto;
import org.example.dto.users.UsersUpdateRequestDTO;
import org.example.enums.AppLanguage;
import org.example.enums.GeneralStatus;
import org.example.enums.Roles;
import org.springframework.data.domain.PageImpl;

public interface UsersService {
    ApiResponse<UsersDTO> getProfile(AppLanguage language);

    ApiResponse<UsersUpdateRequestDTO> updateProfile(UsersUpdateRequestDTO profileDTO, AppLanguage language);


    ApiResponse<String> updatePhoto(UsersUpdatePhoto photoId);

    ApiResponse<UsersUpdatePhoto> getProfilePhoto();

    PageImpl<UsersResponse> getUsers(String q, GeneralStatus status, Roles roles, int page, int perPage);

    ApiResponse<String> setAdmin(Long userId, Roles role);

    ApiResponse<AdminUserDetailResponse> getUserById(Long userId);

    ApiResponse<String> blockUser(Long userId, String reason);

    ApiResponse<String> unblockUser(Long userId);

    ApiResponse<String> revokeUserSessions(Long userId);
}
