package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.service.ChatService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/chats")
public class ChatInternalController {

    private final ChatService chatService;

    @PutMapping("/{threadId}/block")
    public void blockThread(@PathVariable Long threadId) {
        chatService.blockThread(threadId);
    }
}
