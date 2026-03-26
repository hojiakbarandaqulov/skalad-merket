package org.example.dto.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatCreateResponse {
    @JsonProperty("thread_id")
    private Long threadId;

    @JsonProperty("is_new")
    private boolean isNew;
}
