package org.example.dto.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ReadReceiptResponse {
    @JsonProperty("thread_id")
    private Long threadId;

    @JsonProperty("message_ids")
    private List<Long> messageIds;

    @JsonProperty("read_by")
    private Long readBy;
}
