package org.example.repository;

import org.example.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductImageRepository extends JpaRepository<ProductImage, String> {
    long countByProduct_Id(Long productId);

    List<ProductImage> findByProduct_IdOrderBySortOrderAscIdAsc(Long productId);

    Optional<ProductImage> findByIdAndProduct_Id(String imageId, Long productId);

    Optional<ProductImage> findFirstByProduct_IdAndIsPrimaryTrueOrderByCreatedDateDesc(Long productId);
}
