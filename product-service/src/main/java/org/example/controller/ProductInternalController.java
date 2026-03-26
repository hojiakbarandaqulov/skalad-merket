package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.internal.ProductInternalSummaryResponse;
import org.example.entity.Product;
import org.example.entity.ProductImage;
import org.example.exp.AppBadException;
import org.example.repository.ProductImageRepository;
import org.example.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/products")
public class ProductInternalController {

    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;

    @Value("${aws.url}")
    private String awsUrl;

    @Value("${aws.bucket-name}")
    private String bucketName;

    @GetMapping("/{productId}/summary")
    public ProductInternalSummaryResponse summary(@PathVariable Long productId) {
        Product product = productRepository.findByIdAndDeletedAtIsNull(productId)
                .orElseThrow(() -> new AppBadException("product.not.found"));

        ProductImage primaryImage = productImageRepository
                .findFirstByProduct_IdAndIsPrimaryTrueOrderByCreatedDateDesc(productId)
                .orElse(null);

        String primaryImageUrl = primaryImage == null ? null : awsUrl + "/" + bucketName + "/" + primaryImage.getStorageKey();

        return ProductInternalSummaryResponse.builder()
                .id(product.getId())
                .companyId(product.getCompanyId())
                .name(product.getName())
                .slug(product.getSlug())
                .price(product.getPrice())
                .currency(product.getCurrency() == null ? null : product.getCurrency().name())
                .primaryImage(primaryImageUrl)
                .build();
    }
}
