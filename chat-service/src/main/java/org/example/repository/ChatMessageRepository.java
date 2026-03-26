package org.example.repository;

import org.example.entity.ChatMessage;
import org.example.enums.ChatParticipantType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    Optional<ChatMessage> findFirstByThread_IdAndDeletedFalseOrderByIdDesc(Long threadId);

    Page<ChatMessage> findByThread_IdAndDeletedFalse(Long threadId, Pageable pageable);

    Page<ChatMessage> findByThread_IdAndIdLessThanAndDeletedFalse(Long threadId, Long beforeId, Pageable pageable);

    List<ChatMessage> findByThread_IdAndIdInAndDeletedFalse(Long threadId, List<Long> ids);

    long countByThread_IdAndDeletedFalseAndSenderTypeAndBuyerReadAtIsNull(Long threadId, ChatParticipantType senderType);

    long countByThread_IdAndDeletedFalseAndSenderTypeAndSellerReadAtIsNull(Long threadId, ChatParticipantType senderType);

    long countByThread_BuyerIdAndThread_DeletedFalseAndDeletedFalseAndSenderTypeAndBuyerReadAtIsNull(Long buyerId, ChatParticipantType senderType);

    long countByThread_SellerCompanyIdInAndThread_DeletedFalseAndDeletedFalseAndSenderTypeAndSellerReadAtIsNull(List<Long> sellerCompanyIds, ChatParticipantType senderType);
}
