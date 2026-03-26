package org.example.dto.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ChatLastMessageResponse {
    private Long id;
    private String body;

    @JsonProperty("attachment_url")
    private String attachmentUrl;

    @JsonProperty("sent_at")
    private LocalDateTime sentAt;

    private String status;
}
