package org.example.repository;

import jakarta.transaction.Transactional;
import org.example.entity.Attach;
import org.example.entity.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Profile, Long>,
        JpaSpecificationExecutor<Profile> {

    boolean existsByUserId(Long userId);

    Optional<Profile> findByUserId(Long userId);

    Optional<Profile> findByUsernameAndDeletedFalse(String username);

    Optional<Profile> findByIdAndDeletedFalse(Long profileId);

    Profile findByUserIdAndDeletedFalse(Long profileId);

    @Transactional(rollbackOn = Exception.class)
    @Modifying
    @Query("update Profile p set p.photo = :photo where p.id = ?1")
    void updatePhoto(Long id, @Param("photo") Attach photo);

}
