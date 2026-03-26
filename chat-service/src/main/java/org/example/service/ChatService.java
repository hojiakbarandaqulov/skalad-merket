package org.example.service;

import org.example.dto.PagedResponse;
import org.example.dto.chat.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ChatService {
    PagedResponse<ChatThreadResponse> getThreads(int page, int perPage);

    ChatCreateResponse createThread(CreateChatRequest request);

    PagedResponse<ChatMessageResponse> getMessages(Long threadId, int page, int perPage, Long beforeId);

    UnreadCountResponse getUnreadCount();

    UploadAttachmentResponse uploadAttachment(Long threadId, MultipartFile file);

    void hideThread(Long threadId);

    WsTokenResponse issueWsToken();

    void validateThreadAccess(Long userId, Long threadId);

    ChatMessageResponse sendMessage(Long userId, Long threadId, String body, String attachmentKey);

    ReadReceiptResponse markMessagesRead(Long userId, Long threadId, List<Long> messageIds);
}
