package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.*;
import org.example.dto.product.ProductResponse;
import org.example.entity.Product;
import org.example.enums.AppLanguage;
import org.example.enums.Currency;
import org.example.enums.ProductModerationStatus;
import org.example.enums.SaleType;
import org.example.repository.ProductRepository;
import org.example.service.CatalogService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CatalogServiceImpl implements CatalogService {
    private final ProductRepository productRepository;
    private final ProductServiceImpl productService;
    private final ModelMapper modelMapper;

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
        List<ProductResponse> featured = productRepository.findTop8ByModerationStatusAndIsActiveTrueAndIsPromotedTrueAndDeletedAtIsNullOrderByCreatedAtDesc(ProductModerationStatus.APPROVED)
                .stream().map(productService::toResponse).toList();
        List<ProductResponse> latest = productRepository.findTop8ByModerationStatusAndIsActiveTrueAndDeletedAtIsNullOrderByCreatedAtDesc(ProductModerationStatus.APPROVED)
                .stream().map(productService::toResponse).toList();
        return CatalogHomepageResponse.builder()
                .featuredProducts(featured)
                .newProducts(latest)
                .banners(List.of())
                .topCategories(latest.stream().map(ProductResponse::getCategoryId).filter(Objects::nonNull).distinct().toList())
                .verifiedCompanies(latest.stream().map(ProductResponse::getCompanyId).filter(Objects::nonNull).distinct().toList())
                .build();
    }

    @Override
    public PageImpl<ProductResponse> getSaleTypeFilterProduct(int page, int perPage, SaleType saleType, AppLanguage language) {
        PageRequest pagable = PageRequest.of(page - 1, perPage);
        Page<Product> product = productRepository.findBySaleType(saleType, pagable);

        List<ProductResponse> list = product.getContent().stream()
                .map(this::toProductResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(list, pagable, product.getTotalElements());
    }

    private ProductResponse toProductResponse(Product p) {
        ProductResponse res = new ProductResponse();
        res.setId(p.getId());
        res.setCompanyId(p.getCompanyId());
        res.setSellerId(p.getSellerId());
        res.setCategoryId(p.getCategoryId());
        res.setName(p.getName());
        res.setSlug(p.getSlug());
        res.setShortDescription(p.getShortDescription());
        res.setDescription(p.getDescription());
        res.setPriceType(p.getPriceType());
        res.setPrice(p.getPrice());
        res.setCurrency(p.getCurrency());
        res.setRegionId(p.getRegionId());
        res.setDistrictId(p.getDistrictId());
        res.setAttributes(p.getAttributesJsonb());
        res.setStatus(p.getModerationStatus());           // PENDING
        res.setIsActive(p.getIsActive());
        res.setIsPromoted(p.getIsPromoted());
        res.setPromotedUntil(p.getPromotedUntil());
        res.setRejectReason(p.getRejectReason());
        res.setViewsCountCache(p.getViewsCountCache());
        res.setFavoritesCountCache(p.getFavoritesCountCache());
        res.setCreatedAt(p.getCreatedAt());
//        res.setImages(p.getImages());           // images
        return res;
    }
    private PagedResponse<ProductResponse> queryProducts(String q, String category, Long regionId, String currency, int page, int perPage) {
        Specification<Product> spec = (root, query, cb) -> cb.and(
                cb.equal(root.get("moderationStatus"), ProductModerationStatus.APPROVED),
                cb.isTrue(root.get("isActive"))
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
        return product.getModerationStatus() == ProductModerationStatus.APPROVED && Boolean.TRUE.equals(product.getIsActive());
    }
}
