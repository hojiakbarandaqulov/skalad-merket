package org.example.dto.notification;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.example.enums.PushPlatform;

@Getter
@Setter
public class PushTokenRequest {
    @NotBlank
    private String token;

    @NotNull
    private PushPlatform platform;
}
