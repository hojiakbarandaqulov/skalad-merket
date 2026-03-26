package org.example.dto.notification;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Builder
public class NotificationResponse {
    private Long id;
    private String type;
    private Map<String, Object> payload;

    @JsonProperty("sent_at")
    private LocalDateTime sentAt;

    @JsonProperty("read_at")
    private LocalDateTime readAt;
}
