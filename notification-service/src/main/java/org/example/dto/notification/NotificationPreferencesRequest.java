package org.example.dto.notification;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationPreferencesRequest {
    @NotNull
    @JsonProperty("in_app")
    private Boolean inApp;

    @NotNull
    private Boolean push;

    @NotNull
    private Boolean email;
}
