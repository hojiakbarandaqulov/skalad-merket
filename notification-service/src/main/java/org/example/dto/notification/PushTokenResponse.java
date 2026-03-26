package org.example.dto.notification;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PushTokenResponse {
    private String token;
    private String platform;
}
