package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.notification.CreateNotificationRequest;
import org.example.dto.notification.NotificationResponse;
import org.example.service.NotificationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/notifications")
public class NotificationInternalController {

    private final NotificationService notificationService;

    @PostMapping
    public NotificationResponse create(@RequestBody @Valid CreateNotificationRequest request) {
        return notificationService.createInternal(request);
    }
}
