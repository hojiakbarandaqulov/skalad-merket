package org.example.repository;

import org.example.entity.CategoryAttribute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryAttributeRepository extends JpaRepository<CategoryAttribute, Long> {
    boolean existsByCategory_IdAndCodeIgnoreCase(Long categoryId, String code);

    Optional<CategoryAttribute> findByIdAndCategory_Id(Long id, Long categoryId);
}
