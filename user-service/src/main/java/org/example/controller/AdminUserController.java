package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.ApiResponse;
import org.example.dto.users.AdminUserBlockRequest;
import org.example.dto.users.AdminUserDetailResponse;
import org.example.dto.users.UsersResponse;
import org.example.enums.GeneralStatus;
import org.example.enums.Roles;
import org.example.service.UsersService;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/admin/users")
public class AdminUserController {
    private final UsersService usersService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<PageImpl<UsersResponse>> getUsers(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) GeneralStatus status,
            @RequestParam(required = false) Roles roles,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(value = "per_page", defaultValue = "20") int perPage) {
        return ApiResponse.successResponse(
                usersService.getUsers(q, status, roles, page, perPage)
        );
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<AdminUserDetailResponse> getUser(@PathVariable Long userId) {
        return usersService.getUserById(userId);
    }

    @PutMapping("/{userId}/block")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<String> blockUser(@PathVariable Long userId,
                                         @RequestBody(required = false) AdminUserBlockRequest request) {
        return usersService.blockUser(userId, request == null ? null : request.getReason());
    }

    @PutMapping("/{userId}/unblock")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<String> unblockUser(@PathVariable Long userId) {
        return usersService.unblockUser(userId);
    }

    @DeleteMapping("/{userId}/sessions")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<String> revokeSessions(@PathVariable Long userId) {
        return usersService.revokeUserSessions(userId);
    }

    @PutMapping("/set-admin/{userId}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ApiResponse<String> setAdmin(@RequestParam(required = false) Roles role, @PathVariable Long userId){
       return usersService.setAdmin(userId,role);
    }

}
