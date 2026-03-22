package org.example.service.impl;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.client.CategoryClient;
import org.example.client.CompanyClient;
import org.example.client.dto.CategorySummaryResponse;
import org.example.client.dto.CategoryValidationResponse;
import org.example.client.dto.CompanyOwnershipResponse;
import org.example.client.dto.CompanySummaryResponse;
import org.example.dto.product.*;
import org.example.entity.Product;
import org.example.entity.ProductImage;
import org.example.enums.*;
import org.example.enums.Currency;
import org.example.event.ProductCreatedEvent;
import org.example.exp.AppBadException;
import org.example.repository.ProductImageRepository;
import org.example.repository.ProductRepository;
import org.example.service.KafkaProducerService;
import org.example.service.ProductService;
import org.example.service.ViewAsyncService;
import org.example.utils.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private static final Set<String> ALLOWED_IMAGE_TYPES = Set.of("image/jpeg", "image/png", "image/webp");
    private static final long MAX_FILE_SIZE = 5L * 1024 * 1024;
    private static final int MAX_IMAGES = 12;

    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final CompanyClient companyClient;
    private final CategoryClient categoryClient;
    private final KafkaProducerService kafkaProducerService;
    private final ViewAsyncService viewAsyncService;

    private final MinioClient minioClient;

    @Value("${aws.bucket-name}")
    private String bucketName;

    @Value("${app.media.base-url}")
    private String mediaBaseUrl;

    @Value("${aws.url}")
    private String url;

    @Transactional
    @Override
    public ProductResponse create(CreateProductRequest request) {
        Long sellerId = requiredSellerId();
        validateCompanyOwnership(request.getCompanyId(), sellerId);
        validateCategory(request.getCategoryId());

        Product product = new Product();
        applyCommonFields(product, request);
        product.setSlug(generateUniqueSlug(request.getName()));
        product.setModerationStatus(ProductModerationStatus.PENDING);
        product.setIsActive(Boolean.TRUE);
        product.setSellerId(sellerId);

        Product saved = productRepository.save(product);

        kafkaProducerService.sendProductCreated(ProductCreatedEvent.builder()
                .productId(saved.getId())
                .companyId(saved.getCompanyId())
                .categoryId(saved.getCategoryId())
                .name(saved.getName())
                .slug(saved.getSlug())
                .price(saved.getPrice())
                .currency(saved.getCurrency().name())
                .moderationStatus(saved.getModerationStatus().name())
                .createdAt(saved.getCreatedDate())
                .build());

        return toResponse(saved);
    }

    @Transactional
    @Override
    public List<ProductImageResponse> uploadImages(Long productId, List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            throw new AppBadException("At least one image is required");
        }

        Product product = getOwnedProduct(productId);
        long existingCount = productImageRepository.countByProduct_Id(productId);
        if (existingCount + files.size() > MAX_IMAGES) {
            throw new AppBadException("Maximum 12 images allowed");
        }

        int nextSortOrder = productImageRepository.findByProduct_IdOrderBySortOrderAscIdAsc(productId)
                .stream()
                .map(ProductImage::getSortOrder)
                .filter(Objects::nonNull)
                .max(Integer::compareTo)
                .orElse(0) + 1;

        boolean hasPrimary = existingCount > 0;

        for (MultipartFile file : files) {
            ImageMeta imageMeta = validateAndReadImage(file);

            // 2. MinIO ga yuklash
            String originalName = file.getOriginalFilename();
            assert originalName != null;
            String extension = originalName.substring(originalName.lastIndexOf('.') + 1);

            String storageKey = UUID.randomUUID().toString();
            /* productId + "/" +*/

            try (InputStream inputStream = file.getInputStream()) {
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(bucketName)
                                .object(storageKey)
                                .stream(inputStream, file.getSize(), -1)
                                .contentType(file.getContentType())
                                .build()
                );
            } catch (Exception e) {
                throw new AppBadException("Failed to upload image: " + e.getMessage());
            }

            ProductImage image = new ProductImage();
            image.setId(storageKey + "." + extension);
            image.setProduct(product);
            image.setStorageKey(storageKey);
            image.setSortOrder(nextSortOrder++);
            image.setIsPrimary(!hasPrimary);
            image.setMimeType(file.getContentType());
            image.setFileSize(file.getSize());
            image.setWidth(imageMeta.width());
            image.setHeight(imageMeta.height());
            productImageRepository.save(image);
            hasPrimary = true;
        }
        return getImages(productId);
    }

    @Transactional
    @Override
    public void setPrimaryImage(Long productId, String imageId) {
        getOwnedProduct(productId);
        ProductImage target = productImageRepository.findByIdAndProduct_Id(imageId, productId)
                .orElseThrow(() -> new AppBadException("Image not found"));

        List<ProductImage> images = productImageRepository.findByProduct_IdOrderBySortOrderAscIdAsc(productId);
        images.forEach(image -> image.setIsPrimary(image.getId().equals(target.getId())));
        productImageRepository.saveAll(images);
    }

    @Transactional
    @Override
    public boolean deleteImage(Long productId, String imageId) {
        getOwnedProduct(productId);
        ProductImage image = productImageRepository.findByIdAndProduct_Id(imageId, productId)
                .orElseThrow(() -> new AppBadException("Image not found"));
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(imageId)
                            .build()
            );
            productImageRepository.delete(image);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("File delete error: " + e.getMessage());
        }
    }

    @Override
    public ProductListResponse getMyProducts(Long companyId, ProductModerationStatus status, int page, int perPage) {
        Long sellerId = requiredSellerId();
        int resolvedPage = normalizePage(page);
        int resolvedPerPage = normalizePerPage(perPage);
        List<Long> ownedCompanyIds = companyClient.getOwnedCompanyIds(sellerId);

        if (ownedCompanyIds.isEmpty()) {
            return ProductListResponse.builder()
                    .items(List.of())
                    .page(resolvedPage)
                    .perPage(resolvedPerPage)
                    .totalElements(0L)
                    .totalPages(0)
                    .build();
        }

        if (companyId != null && !ownedCompanyIds.contains(companyId)) {
            throw new AppBadException("You do not own this company");
        }

        Pageable pageable = PageRequest.of(resolvedPage - 1, resolvedPerPage, Sort.by(Sort.Direction.DESC, "createdDate"));
        Specification<Product> specification = notDeleted();
        specification = specification.and((root, query, cb) -> root.get("companyId").in(companyId != null ? List.of(companyId) : ownedCompanyIds));

        if (status != null) {
            specification = specification.and((root, query, cb) -> cb.equal(root.get("moderationStatus"), status));
        }

        Page<Product> productPage = productRepository.findAll(specification, pageable);
        return ProductListResponse.builder()
                .items(productPage.getContent().stream().map(this::toResponse).collect(Collectors.toList()))
                .page(resolvedPage)
                .perPage(resolvedPerPage)
                .totalElements(productPage.getTotalElements())
                .totalPages(productPage.getTotalPages())
                .build();
    }

    @Override
    public ProductDetailResponse getPublicDetail(String slug, String sessionId) {
        Product product = productRepository.findBySlugAndModerationStatusAndIsActiveTrueAndDeletedAtIsNull(slug, ProductModerationStatus.APPROVED)
                .orElseThrow(() -> new AppBadException("Product not found"));

        CompanySummaryResponse company = companyClient.getSummary(product.getCompanyId());
        CategorySummaryResponse category = categoryClient.getSummary(product.getCategoryId());

        viewAsyncService.logView(product.getId(), SpringSecurityUtil.getProfileId(), sessionId);

        return ProductDetailResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .slug(product.getSlug())
                .shortDescription(product.getShortDescription())
                .description(product.getDescription())
                .priceType(product.getPriceType())
                .price(product.getPrice())
                .currency(product.getCurrency())
                .images(getImages(product.getId()))
                .attributes(product.getAttributesJsonb())
                .status(product.getModerationStatus())
                .isPromoted(product.getIsPromoted())
                .viewsCountCache(product.getViewsCountCache())
                .favoritesCountCache(product.getFavoritesCountCache())
                .createdAt(product.getCreatedDate())
                .company(ProductDetailResponse.CompanySummary.builder()
                        .id(company.getId())
                        .name(company.getName())
                        .slug(company.getSlug())
                        .logoPath(company.getLogoPath())
                        .build())
                .category(ProductDetailResponse.CategorySummary.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .slug(category.getSlug())
                        .build())
                .regionId(product.getRegionId())
                .districtId(product.getDistrictId())
                .similarProducts(productRepository
                        .findTop8ByCategoryIdAndIdNotAndModerationStatusAndIsActiveTrueAndDeletedAtIsNullOrderByCreatedDateDesc(
                                product.getCategoryId(),
                                product.getId(),
                                ProductModerationStatus.APPROVED
                        )
                        .stream()
                        .map(this::toSimilarProduct)
                        .collect(Collectors.toList()))
                .build();
    }

    @Transactional
    @Override
    public ProductResponse update(Long id, UpdateProductRequest request) {
        Product product = getOwnedProduct(id);
        validateCompanyOwnership(request.getCompanyId(), requiredSellerId());
        validateCategory(request.getCategoryId());

        String previousName = product.getName();
        applyCommonFields(product, request);
        if (!previousName.equalsIgnoreCase(request.getName())) {
            product.setSlug(generateUniqueSlug(request.getName()));
        }
        if (product.getModerationStatus() == ProductModerationStatus.APPROVED) {
            product.setModerationStatus(ProductModerationStatus.PENDING);
        }

        return toResponse(productRepository.save(product));
    }

    @Transactional
    @Override
    public void publish(Long id) {
        Product product = getOwnedProduct(id);
        if (product.getModerationStatus() != ProductModerationStatus.PENDING) {
            throw new AppBadException("Only draft products can be published");
        }
        product.setModerationStatus(ProductModerationStatus.PENDING);
        productRepository.save(product);
    }

    @Transactional
    @Override
    public void archive(Long id) {
        Product product = getOwnedProduct(id);
        if (product.getModerationStatus() != ProductModerationStatus.APPROVED) {
            throw new AppBadException("Only approved products can be archived");
        }
        product.setModerationStatus(ProductModerationStatus.ARCHIVED);
        product.setIsActive(Boolean.FALSE);
        productRepository.save(product);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        Product product = productRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new AppBadException("Product not found"));

        if (!SpringSecurityUtil.hasRole("ADMIN") && !SpringSecurityUtil.hasRole("SUPER_ADMIN")) {
            validateCompanyOwnership(product.getCompanyId(), requiredSellerId());
        }

        product.setDeletedAt(LocalDateTime.now());
        product.setIsActive(Boolean.FALSE);
        productRepository.save(product);
    }

    @Override
    public ProductResponse getById(Long id) {
        Optional<Product> byIdAndIsActiveTrue = productRepository.findByIdAndIsActiveTrue(id);
        return byIdAndIsActiveTrue.map(this::toResponse).orElse(null);
    }

    private BigDecimal normalizePrice(PriceType priceType, BigDecimal price) {
        if (priceType == PriceType.NEGOTIABLE) {
            return null;
        }
        if (price == null || price.signum() <= 0) {
            throw new AppBadException("Price is required for fixed and from_price products");
        }
        return price;
    }

    private void validateCompanyOwnership(Long companyId, Long sellerId) {
        CompanyOwnershipResponse response = companyClient.checkOwnership(companyId, sellerId);
        if (!response.isExists()) {
            throw new AppBadException("Company not found");
        }
        if (!response.isOwner()) {
            throw new AppBadException("You do not own this company");
        }
        if (!response.isActive()) {
            throw new AppBadException("Company is not active or verified");
        }
    }

    private void validateCategory(Long categoryId) {
        CategoryValidationResponse response = categoryClient.validate(categoryId);
        if (!response.isExists()) {
            throw new AppBadException("Category not found");
        }
        if (!response.isActive()) {
            throw new AppBadException("Category is inactive");
        }
    }

    private Product getOwnedProduct(Long productId) {
        Product product = productRepository.findByIdAndDeletedAtIsNull(productId)
                .orElseThrow(() -> new AppBadException("Product not found"));
        validateCompanyOwnership(product.getCompanyId(), requiredSellerId());
        return product;
    }

    private Long requiredSellerId() {
        Long sellerId = SpringSecurityUtil.getProfileId();
        if (sellerId == null) {
            throw new AppBadException("Seller profile not found in token");
        }
        return sellerId;
    }

    private void applyCommonFields(Product product, CreateProductRequest request) {
        product.setCompanyId(request.getCompanyId());
        product.setCategoryId(request.getCategoryId());
        product.setName(request.getName());
        product.setShortDescription(request.getShortDescription());
        product.setDescription(request.getDescription());
        product.setPriceType(request.getPriceType());
        product.setPrice(normalizePrice(request.getPriceType(), request.getPrice()));
        product.setCurrency(request.getCurrency());
        product.setRegionId(request.getRegionId());
        product.setDistrictId(request.getDistrictId());
        product.setAttributesJsonb(normalizeAttributes(request.getAttributes()));
    }

    private void applyCommonFields(Product product, UpdateProductRequest request) {
        product.setCompanyId(request.getCompanyId());
        product.setCategoryId(request.getCategoryId());
        product.setName(request.getName());
        product.setShortDescription(request.getShortDescription());
        product.setDescription(request.getDescription());
        product.setPriceType(request.getPriceType());
        product.setPrice(normalizePrice(request.getPriceType(), request.getPrice()));
        product.setCurrency(request.getCurrency());
        product.setRegionId(request.getRegionId());
        product.setDistrictId(request.getDistrictId());
        product.setAttributesJsonb(normalizeAttributes(request.getAttributes()));
    }

    private String generateUniqueSlug(String name) {
        String base = slugify(name);
        String candidate = base;
        int counter = 1;
        while (productRepository.existsBySlug(candidate)) {
            candidate = base + "-" + counter++;
        }
        return candidate;
    }

    private String slugify(String value) {
        String normalized = Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "");
        String slug = normalized.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");
        return StringUtils.hasText(slug) ? slug : "product";
    }

    private ImageMeta validateAndReadImage(MultipartFile file) {
        if (file.isEmpty()) {
            throw new AppBadException("Empty file is not allowed");
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new AppBadException("Each image must be <= 5MB");
        }
        if (!ALLOWED_IMAGE_TYPES.contains(file.getContentType())) {
            throw new AppBadException("Only jpg, jpeg, png, webp images are allowed");
        }
        try {
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            if (bufferedImage == null) {
                throw new AppBadException("Invalid image file");
            }

            return new ImageMeta(bufferedImage.getWidth(), bufferedImage.getHeight());
        } catch (IOException e) {
            throw new AppBadException("Failed to read image");
        }
    }

    private String generateStorageKey(Long productId, String originalFilename) {
        String cleanName = StringUtils.hasText(originalFilename) ? originalFilename.replaceAll("\\s+", "-") : "image";
        return "products/" + productId + "/" + UUID.randomUUID() + "-" + cleanName;
    }


    private List<ProductImageResponse> getImages(Long productId) {
        return productImageRepository.findByProduct_IdOrderBySortOrderAscIdAsc(productId)
                .stream()
                .map(this::toImageResponse)
                .collect(Collectors.toList());
    }

    private ProductImageResponse toImageResponse(ProductImage image) {
        String originalUrl = url + "/" + bucketName + "/" + image.getStorageKey();
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

    private ProductDetailResponse.SimilarProductResponse toSimilarProduct(Product product) {
        List<ProductImage> images = productImageRepository
                .findByProduct_IdOrderBySortOrderAscIdAsc(product.getId());

        String primaryImage = null;
        for (ProductImage image : images) {
            if (Boolean.TRUE.equals(image.getIsPrimary())) {
                primaryImage = url + "/" + bucketName + "/" + image.getStorageKey();
                break;
            }
        }
        return ProductDetailResponse.SimilarProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .slug(product.getSlug())
                .price(product.getPrice())
                .currency(product.getCurrency())
                .isPromoted(product.getIsPromoted())
                .primaryImage(primaryImage)
                .build();
    }

    private Specification<Product> notDeleted() {
        return (root, query, cb) -> cb.isNull(root.get("deletedAt"));
    }

    private Map<String, Object> normalizeAttributes(Map<String, Object> attributes) {
        return attributes == null ? new HashMap<>() : new HashMap<>(attributes);
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

    private record ImageMeta(int width, int height) {
    }

    public ProductResponse toResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setSellerId(product.getSellerId());
        response.setCompanyId(product.getCompanyId());
        response.setCategoryId(product.getCategoryId());
        response.setName(product.getName());
        response.setShortDescription(product.getShortDescription());
        response.setDescription(product.getDescription());
        response.setPriceType(product.getPriceType());
        response.setPrice(product.getPrice());
        if (product.getCurrency() != null) {
            response.setCurrency(Currency.valueOf(String.valueOf(product.getCurrency())));
        }
        response.setRegionId(product.getRegionId());
        response.setDistrictId(product.getDistrictId());
        response.setStatus(resolveStatus(product));
        response.setAttributes(ServiceHelper.readAttributes(product.getAttributesJsonb().toString()));
        return response;
    }

    private ProductModerationStatus resolveStatus(Product product) {
        if (Boolean.TRUE.equals(product.getIsActive())) {
            return ProductModerationStatus.ARCHIVED;
        }
       else if (Boolean.TRUE.equals(product.getIsActive()) && product.getModerationStatus() == ProductModerationStatus.APPROVED) {
            return ProductModerationStatus.APPROVED;
        }
        if (Boolean.FALSE.equals(product.getIsActive())) {
            return ProductModerationStatus.ARCHIVED;
        }

        return ProductModerationStatus.PENDING;
    }

    /*private void fillProduct(Product product, org.example.dto.CreateProductRequest request) {
        product.setCompanyId(request.getCompanyId());
        product.setCategoryId(request.getCategoryId());
        product.setName(request.getName());
        product.setShortDescription(request.getShortDescription());
        product.setDescription(request.getDescription());
        product.setPriceType(request.getPriceType());
        product.setPrice(request.getPrice());
        product.setCurrency(Currency.valueOf(request.getCurrency().name()));
        product.setRegionId(request.getRegionId());
        product.setDistrictId(request.getDistrictId());
        product.setAttributesJsonb(ServiceHelper.writeAttributes(request.getAttributes()));
    }*/
}
