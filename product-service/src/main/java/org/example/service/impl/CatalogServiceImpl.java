package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.*;
import org.example.dto.product.ProductResponse;
import org.example.entity.Product;
import org.example.enums.AppLanguage;
import org.example.enums.Currency;
import org.example.enums.ModerationStatus;
import org.example.enums.ProductModerationStatus;
import org.example.repository.ProductRepository;
import org.example.service.CatalogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CatalogServiceImpl implements CatalogService {
    private final ProductRepository productRepository;
    private final ProductServiceImpl productService;

    @Override
    public PagedResponse<ProductResponse> getCatalog(String q, String category, Long regionId, String currency, int page, int perPage, AppLanguage language) {
        return queryProducts(q, category, regionId, currency, page, perPage);
    }

    @Override
    public PagedResponse<ProductResponse> search(String q, String category, Long regionId, int page, int perPage, AppLanguage language) {
        return queryProducts(q, category, regionId, null, page, perPage);
    }

    @Override
    public SuggestionResponse suggestions(String q, AppLanguage language) {
        if (q == null || q.trim().length() < 2) {
            return new SuggestionResponse(List.of());
        }
        List<String> suggestions = productRepository.findAll().stream()
                .filter(this::isVisible)
                .map(Product::getName)
                .filter(Objects::nonNull)
                .filter(name -> name.toLowerCase().contains(q.toLowerCase()))
                .distinct()
                .limit(10)
                .toList();
        return new SuggestionResponse(suggestions);
    }

    @Override
    public CatalogFilterResponse filters(String category, AppLanguage language) {
        List<Product> products = productRepository.findAll().stream()
                .filter(this::isVisible)
                .filter(product -> category == null || category.isBlank() || category.equals(String.valueOf(product.getCategoryId())))
                .toList();

        BigDecimal minPrice = products.stream().map(Product::getPrice).filter(Objects::nonNull).min(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
        BigDecimal maxPrice = products.stream().map(Product::getPrice).filter(Objects::nonNull).max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
        List<Long> regionIds = products.stream().map(Product::getRegionId).filter(Objects::nonNull).distinct().toList();
        Map<String, List<String>> attributes = new LinkedHashMap<>();
        for (Product product : products) {
            ServiceHelper.readAttributes(product.getAttributesJsonb().toString()).forEach((key, value) -> attributes.computeIfAbsent(key, k -> new ArrayList<>()).add((String) value));
        }
        attributes.replaceAll((key, values) -> values.stream().distinct().toList());
        return CatalogFilterResponse.builder().minPrice(minPrice).maxPrice(maxPrice).regionIds(regionIds).attributes(attributes).build();
    }

    @Override
    public CatalogHomepageResponse homepage(AppLanguage language) {
        List<ProductResponse> featured = productRepository.findTop8ByModerationStatusAndIsActiveTrueAndIsPromotedTrueAndDeletedAtIsNullOrderByCreatedDateDesc(ProductModerationStatus.APPROVED)
                .stream().map(productService::toResponse).toList();
        List<ProductResponse> latest = productRepository.findTop8ByModerationStatusAndIsActiveTrueAndDeletedAtIsNullOrderByCreatedDateDesc(ProductModerationStatus.APPROVED)
                .stream().map(productService::toResponse).toList();
        return CatalogHomepageResponse.builder()
                .featuredProducts(featured)
                .newProducts(latest)
                .banners(List.of())
                .topCategoryIds(latest.stream().map(ProductResponse::getCategoryId).filter(Objects::nonNull).distinct().toList())
                .verifiedCompanyIds(latest.stream().map(ProductResponse::getCompanyId).filter(Objects::nonNull).distinct().toList())
                .build();
    }

    private PagedResponse<ProductResponse> queryProducts(String q, String category, Long regionId, String currency, int page, int perPage) {
        Specification<Product> spec = (root, query, cb) -> cb.and(
                cb.equal(root.get("moderationStatus"), ModerationStatus.APPROVED),
                cb.isTrue(root.get("isActive")),
                cb.isFalse(root.get("deleted"))
        );
        if (q != null && !q.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("name")), "%" + q.toLowerCase() + "%"));
        }
        if (category != null && !category.isBlank()) {
            try {
                Long categoryId = Long.valueOf(category);
                spec = spec.and((root, query, cb) -> cb.equal(root.get("categoryId"), categoryId));
            } catch (NumberFormatException ignored) {
            }
        }
        if (regionId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("regionId"), regionId));
        }
        if (currency != null && !currency.isBlank()) {
            String normalized = Currency.valueOf(currency.toUpperCase()).name();
            spec = spec.and((root, query, cb) -> cb.equal(root.get("currency"), normalized));
        }
        Page<Product> result = productRepository.findAll(spec, PageRequest.of(Math.max(page - 1, 0), perPage));
        return ServiceHelper.toPagedResponse(result.map(productService::toResponse));
    }

    private boolean isVisible(Product product) {
        return product.getModerationStatus() == ProductModerationStatus.APPROVED && Boolean.TRUE.equals(product.getIsActive()) && Boolean.FALSE.equals(product.getIsActive());
    }
}
