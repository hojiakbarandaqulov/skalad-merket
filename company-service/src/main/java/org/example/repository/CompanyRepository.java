package org.example.repository;

import org.example.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    Long countByOwnerUserIdAndDeletedAtIsNull(Long userId);
}
