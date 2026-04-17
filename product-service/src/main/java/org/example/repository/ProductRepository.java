package org.example.repository;

import org.example.entity.Product;
import org.example.enums.ProductModerationStatus;
import org.example.enums.SaleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByIdAndDeletedAtIsNull(Long id);

    Optional<Product> findBySlugAndModerationStatusAndIsActiveTrueAndDeletedAtIsNull(String slug, ProductModerationStatus moderationStatus);

    boolean existsBySlug(String slug);

    List<Product> findTop8ByCategoryIdAndIdNotAndModerationStatusAndIsActiveTrueAndDeletedAtIsNullOrderByCreatedAtDesc(
            Long categoryId,
            Long productId,
            ProductModerationStatus moderationStatus
    );
    List<Product> findTop8ByModerationStatusAndIsActiveTrueAndDeletedAtIsNullOrderByCreatedAtDesc(ProductModerationStatus moderationStatus);

    List<Product> findTop8ByModerationStatusAndIsActiveTrueAndIsPromotedTrueAndDeletedAtIsNullOrderByCreatedAtDesc(ProductModerationStatus moderationStatus);


    Page<Product> findAll(Specification<Product> specification, Pageable pageable);

     Optional<Product> findByIdAndIsActiveTrue(Long productId);

    Page<Product> findBySaleType(SaleType saleType, Pageable pageable);
}
