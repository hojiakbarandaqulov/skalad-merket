package org.example.dto.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MarkReadRequest {
    @NotEmpty
    @JsonProperty("message_ids")
    private List<Long> messageIds;
}
