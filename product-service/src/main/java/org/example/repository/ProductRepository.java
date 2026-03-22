package org.example.repository;

import org.example.entity.Product;
import org.example.enums.ProductModerationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByIdAndDeletedAtIsNull(Long id);

    Optional<Product> findBySlugAndModerationStatusAndIsActiveTrueAndDeletedAtIsNull(String slug, ProductModerationStatus moderationStatus);

    boolean existsBySlug(String slug);

    List<Product> findTop8ByCategoryIdAndIdNotAndModerationStatusAndIsActiveTrueAndDeletedAtIsNullOrderByCreatedDateDesc(
            Long categoryId,
            Long productId,
            ProductModerationStatus moderationStatus
    );
    List<Product> findTop8ByModerationStatusAndIsActiveTrueAndDeletedAtIsNullOrderByCreatedDateDesc(ProductModerationStatus moderationStatus);

    List<Product> findTop8ByModerationStatusAndIsActiveTrueAndIsPromotedTrueAndDeletedAtIsNullOrderByCreatedDateDesc(ProductModerationStatus moderationStatus);


    Page<Product> findAll(Specification<Product> specification, Pageable pageable);

    Optional<Product> findByIdAndIsActiveTrue(Long productId);
}
