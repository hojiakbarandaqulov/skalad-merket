package org.example.dto.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatParticipantResponse {
    private Long id;
    private String type;

    @JsonProperty("display_name")
    private String displayName;

    private String username;
    private String slug;

    @JsonProperty("avatar_url")
    private String avatarUrl;
}
