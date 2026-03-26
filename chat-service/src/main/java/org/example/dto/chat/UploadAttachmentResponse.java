package org.example.dto.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UploadAttachmentResponse {
    @JsonProperty("attachment_key")
    private String attachmentKey;

    @JsonProperty("attachment_url")
    private String attachmentUrl;
}
