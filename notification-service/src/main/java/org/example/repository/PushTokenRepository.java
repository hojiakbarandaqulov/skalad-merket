package org.example.repository;

import org.example.entity.PushToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PushTokenRepository extends JpaRepository<PushToken, Long> {
    Optional<PushToken> findByTokenAndDeletedFalse(String token);
}
