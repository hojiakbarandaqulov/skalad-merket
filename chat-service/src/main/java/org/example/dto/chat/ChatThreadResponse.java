package org.example.dto.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatThreadResponse {
    @JsonProperty("thread_id")
    private Long threadId;

    @JsonProperty("other_party")
    private ChatParticipantResponse otherParty;

    @JsonProperty("last_message")
    private ChatLastMessageResponse lastMessage;

    @JsonProperty("unread_count")
    private long unreadCount;

    private ChatProductSummaryResponse product;
}
