package org.example.repository;

import org.example.entity.ChatThread;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatThreadRepository extends JpaRepository<ChatThread, Long> {

    @Query("""
            select t from ChatThread t
            where t.deleted = false
              and t.buyerId = :buyerId
              and t.sellerCompanyId = :sellerCompanyId
              and ((:productId is null and t.productId is null) or t.productId = :productId)
            """)
    Optional<ChatThread> findUnique(@Param("buyerId") Long buyerId,
                                    @Param("sellerCompanyId") Long sellerCompanyId,
                                    @Param("productId") Long productId);

    List<ChatThread> findByBuyerIdAndBuyerHiddenFalseAndDeletedFalse(Long buyerId, Sort sort);

    List<ChatThread> findBySellerCompanyIdInAndSellerHiddenFalseAndDeletedFalse(List<Long> sellerCompanyIds, Sort sort);

    Optional<ChatThread> findByIdAndDeletedFalse(Long id);
}
