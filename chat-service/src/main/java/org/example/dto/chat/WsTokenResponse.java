package org.example.dto.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WsTokenResponse {
    @JsonProperty("ws_token")
    private String wsToken;

    @JsonProperty("expires_in")
    private long expiresIn;
}
