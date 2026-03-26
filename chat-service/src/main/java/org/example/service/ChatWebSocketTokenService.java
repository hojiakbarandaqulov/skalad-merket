package org.example.service;

import org.example.dto.chat.WsTokenResponse;

public interface ChatWebSocketTokenService {
    WsTokenResponse issueToken();

    Long parseToken(String token);
}
