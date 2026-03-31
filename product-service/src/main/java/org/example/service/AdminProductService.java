package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.admin.ModerationDecisionRequest;
import org.example.dto.admin.PromotionRequest;
import org.example.dto.admin.ReasonRequest;
import org.example.dto.product.ProductImageResponse;
import org.example.dto.product.ProductListResponse;
import org.example.dto.product.ProductResponse;
import org.example.entity.Product;
import org.example.entity.ProductImage;
import org.example.enums.ProductModerationStatus;
import org.example.exp.AppBadException;
import org.example.repository.ProductImageRepository;
import org.example.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminProductService {

    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;

    @Value("${aws.url}")
    private String awsUrl;

    @Value("${aws.bucket-name}")
    private String bucketName;

    public ProductListResponse getProducts(ProductModerationStatus status, Long companyId, String q, int page, int perPage) {
        int resolvedPage = normalizePage(page);
        int resolvedPerPage = normalizePerPage(perPage);

        Specification<Product> specification = Specification.where(null);
        if (status != null) {
            specification = specification.and((root, query, cb) -> cb.equal(root.get("moderationStatus"), status));
        }
        if (companyId != null) {
            specification = specification.and((root, query, cb) -> cb.equal(root.get("companyId"), companyId));
        }
        if (StringUtils.hasText(q)) {
            String like = "%" + q.trim().toLowerCase() + "%";
            specification = specification.and((root, query, cb) -> cb.or(
                    cb.like(cb.lower(root.get("name")), like),
                    cb.like(cb.lower(root.get("slug")), like),
                    cb.like(cb.lower(root.get("description")), like)
            ));
        }

        PageRequest pageRequest = PageRequest.of(resolvedPage - 1, resolvedPerPage, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<Product> result = productRepository.findAll(specification, pageRequest);
        return ProductListResponse.builder()
                .items(result.getContent().stream().map(this::toResponse).toList())
                .page(resolvedPage)
                .perPage(resolvedPerPage)
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .build();
    }

    public List<ProductResponse> getModerationQueue() {
        Specification<Product> specification = Specification.where(notDeleted())
                .and((root, query, cb) -> cb.equal(root.get("moderationStatus"), ProductModerationStatus.PENDING));

        return productRepository.findAll(specification, Sort.by(Sort.Direction.ASC, "createdDate"))
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public void approve(Long id) {
        Product product = getProduct(id);
        if (product.getDeletedAt() != null) {
            throw new AppBadException("Product is deleted");
        }
        product.setModerationStatus(ProductModerationStatus.APPROVED);
        product.setRejectReason(null);
        product.setIsActive(Boolean.TRUE);
        productRepository.save(product);
    }

    @Transactional
    public void reject(Long id, ModerationDecisionRequest request) {
        Product product = getProduct(id);
        product.setModerationStatus(ProductModerationStatus.REJECTED);
        product.setRejectReason(resolveReason(request));
        productRepository.save(product);
    }

    @Transactional
    public void block(Long id, ReasonRequest request) {
        Product product = getProduct(id);
        product.setIsActive(Boolean.FALSE);
        if (StringUtils.hasText(request == null ? null : request.getReason())) {
            product.setRejectReason(request.getReason().trim());
        }
        productRepository.save(product);
    }

    @Transactional
    public void promote(Long id, PromotionRequest request) {
        Product product = getProduct(id);
        product.setIsPromoted(Boolean.TRUE);
        product.setPromotedUntil(request == null ? null : request.getEndsAt());
        productRepository.save(product);
    }

    private Product getProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new AppBadException("Product not found"));
    }

    private ProductResponse toResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setCompanyId(product.getCompanyId());
        response.setSellerId(product.getSellerId());
        response.setCategoryId(product.getCategoryId());
        response.setName(product.getName());
        response.setSlug(product.getSlug());
        response.setShortDescription(product.getShortDescription());
        response.setDescription(product.getDescription());
        response.setPriceType(product.getPriceType());
        response.setPrice(product.getPrice());
        response.setCurrency(product.getCurrency());
        response.setRegionId(product.getRegionId());
        response.setDistrictId(product.getDistrictId());
        response.setAttributes(product.getAttributesJsonb());
        response.setStatus(product.getModerationStatus());
        response.setIsActive(product.getIsActive());
        response.setIsPromoted(product.getIsPromoted());
        response.setPromotedUntil(product.getPromotedUntil());
        response.setRejectReason(product.getRejectReason());
        response.setViewsCountCache(product.getViewsCountCache());
        response.setFavoritesCountCache(product.getFavoritesCountCache());
        response.setCreatedAt(product.getCreatedDate());
        response.setUpdatedAt(product.getModifiedDate());
        response.setImages(getImages(product.getId()));
        return response;
    }

    private List<ProductImageResponse> getImages(Long productId) {
        return productImageRepository.findByProduct_IdOrderBySortOrderAscIdAsc(productId)
                .stream()
                .map(this::toImageResponse)
                .toList();
    }

    private ProductImageResponse toImageResponse(ProductImage image) {
        String originalUrl = awsUrl + "/" + bucketName + "/" + image.getStorageKey();
        return ProductImageResponse.builder()
                .id(image.getId())
                .url(originalUrl)
                .thumbnailUrls(Map.of(
                        "320", originalUrl + "?w=320",
                        "640", originalUrl + "?w=640",
                        "960", originalUrl + "?w=960"
                ))
                .isPrimary(image.getIsPrimary())
                .build();
    }

    private Specification<Product> notDeleted() {
        return (root, query, cb) -> cb.isNull(root.get("deletedAt"));
    }

    private String resolveReason(ModerationDecisionRequest request) {
        if (request == null) {
            return null;
        }
        boolean hasReasonCode = StringUtils.hasText(request.getReasonCode());
        boolean hasComment = StringUtils.hasText(request.getComment());
        if (hasReasonCode && hasComment) {
            return request.getReasonCode().trim() + ": " + request.getComment().trim();
        }
        if (hasReasonCode) {
            return request.getReasonCode().trim();
        }
        if (hasComment) {
            return request.getComment().trim();
        }
        return null;
    }

    private int normalizePage(int page) {
        if (page < 1) {
            throw new AppBadException("page must be greater than or equal to 1");
        }
        return page;
    }

    private int normalizePerPage(int perPage) {
        if (perPage < 1 || perPage > 100) {
            throw new AppBadException("per_page must be between 1 and 100");
        }
        return perPage;
    }
}
