package org.example.service;

import org.example.dto.product.*;
import org.example.enums.ProductModerationStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    ProductResponse create(CreateProductRequest request);

    List<ProductImageResponse> uploadImages(Long productId, List<MultipartFile> files);

    void setPrimaryImage(Long productId, String imageId);

    boolean deleteImage(Long productId, String imageId);

    ProductListResponse getMyProducts(Long companyId, ProductModerationStatus status, int page, int perPage);

    ProductDetailResponse getPublicDetail(String slug, String sessionId);

    ProductResponse update(Long id, UpdateProductRequest request);

    void publish(Long id);

    void archive(Long id);

    void delete(Long id);
}
