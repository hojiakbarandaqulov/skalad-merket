package org.example.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.example.entity.base.BaseEntity;
import org.example.enums.ChatParticipantType;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "chat_messages")
public class ChatMessage extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "thread_id", nullable = false)
    private ChatThread thread;

    private Long senderId;

    @Enumerated(EnumType.STRING)
    private ChatParticipantType senderType;

    @Lob
    private String body;

    private String attachmentKey;
    private String attachmentUrl;
    private LocalDateTime sentAt;
    private LocalDateTime deliveredAt;
    private LocalDateTime buyerReadAt;
    private LocalDateTime sellerReadAt;
}
