package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.ApiResponse;
import org.example.dto.PagedResponse;
import org.example.dto.notification.*;
import org.example.service.NotificationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ApiResponse<PagedResponse<NotificationResponse>> getNotifications(
            @RequestParam(value = "is_read", required = false) Boolean isRead,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(value = "per_page", defaultValue = "20") int perPage) {
        return ApiResponse.successResponse(notificationService.getNotifications(isRead, page, perPage));
    }

    @PostMapping("/mark-read")
    public ApiResponse<Void> markRead(@RequestBody MarkReadRequest request) {
        notificationService.markRead(request);
        return ApiResponse.successResponse();
    }

    @GetMapping("/preferences")
    public ApiResponse<NotificationPreferencesResponse> getPreferences() {
        return ApiResponse.successResponse(notificationService.getPreferences());
    }

    @PutMapping("/preferences")
    public ApiResponse<NotificationPreferencesResponse> updatePreferences(
            @RequestBody @Valid NotificationPreferencesRequest request) {
        return ApiResponse.successResponse(notificationService.updatePreferences(request));
    }

    @PostMapping("/push-token")
    public ApiResponse<PushTokenResponse> registerPushToken(@RequestBody @Valid PushTokenRequest request) {
        return ApiResponse.successResponse(notificationService.registerPushToken(request));
    }
}
