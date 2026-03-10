package org.example.repository;

import org.example.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

    boolean existsByUserId(Long userId);

    Optional<Profile> findByUserId(Long userId);
}
