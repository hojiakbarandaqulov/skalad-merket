package org.example.dto.notification;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotificationPreferencesResponse {
    @JsonProperty("in_app")
    private Boolean inApp;

    private Boolean push;
    private Boolean email;
}
