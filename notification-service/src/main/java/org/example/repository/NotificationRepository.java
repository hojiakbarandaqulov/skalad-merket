package org.example.repository;

import org.example.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<Notification> findByUserIdAndDeletedFalse(Long userId, Pageable pageable);

    Page<Notification> findByUserIdAndReadAtIsNullAndDeletedFalse(Long userId, Pageable pageable);

    Page<Notification> findByUserIdAndReadAtIsNotNullAndDeletedFalse(Long userId, Pageable pageable);

    List<Notification> findByUserIdAndIdInAndDeletedFalse(Long userId, List<Long> ids);

    List<Notification> findByUserIdAndReadAtIsNullAndDeletedFalse(Long userId);
}
