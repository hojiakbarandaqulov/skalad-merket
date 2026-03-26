package org.example.dto.notification;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class CreateNotificationRequest {
    @NotNull
    @JsonProperty("user_id")
    private Long userId;

    @NotBlank
    private String type;

    @NotNull
    private Map<String, Object> payload;
}
