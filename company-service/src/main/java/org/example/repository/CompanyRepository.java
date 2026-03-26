package org.example.repository;

import org.example.entity.Company;
import org.example.enums.VerificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    Long countByOwnerUserIdAndDeletedAtIsNull(Long userId);

    Optional<Company> findByOwnerUserIdAndDeletedFalse(Long userId);

    Company findBySlugAndDeletedAtIsNullAndVerificationStatusIn(String slug, List<VerificationStatus> verified);

   Optional<Company> findByIdAndDeletedFalse(Long id);

    Optional<Company> findByIdAndOwnerUserIdAndDeletedAtIsNull(Long id,Long ownerId);

    Optional<Company> findByIdAndDeletedAtIsNull(Long id);

    List<Company> findAllByOwnerUserIdAndDeletedAtIsNull(Long ownerUserId);
}
