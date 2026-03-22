package org.example.repository;

import org.example.entity.LeadItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeadItemRepository extends JpaRepository<LeadItem, Long> {
    List<LeadItem> findByLeadIdAndDeletedFalse(Long leadId);

    long countByProductIdAndDeletedFalse(Long productId);
}
