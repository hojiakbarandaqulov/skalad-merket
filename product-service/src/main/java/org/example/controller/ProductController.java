package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.ApiResponse;
import org.example.dto.product.*;
import org.example.enums.ProductModerationStatus;
import org.example.service.ProductService;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @PreAuthorize("hasRole('SELLER')")
    public ApiResponse<ProductResponse> create(@RequestBody @Valid CreateProductRequest request) {
        return ApiResponse.successResponse(productService.create(request));
    }

    @PostMapping(value = "/{id}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('SELLER')")
    public ApiResponse<List<ProductImageResponse>> uploadImages(@PathVariable Long id,
                                                                @RequestParam(value = "files") List<MultipartFile> files) {
        return ApiResponse.successResponse(productService.uploadImages(id, files));
    }

    @PutMapping("/{id}/images/{imageId}/set-primary")
    @PreAuthorize("hasRole('SELLER')")
    public ApiResponse<Map<String, String>> setPrimary(@PathVariable Long id, @PathVariable String imageId) {
        productService.setPrimaryImage(id, imageId);
        return ApiResponse.successResponse(Map.of("message", "Primary image updated"));
    }

    @DeleteMapping("/{id}/images/{imageId}")
    @PreAuthorize("hasRole('SELLER')")
    public ApiResponse<Map<String, String>> deleteImage(@PathVariable Long id, @PathVariable String imageId) {
        productService.deleteImage(id, imageId);
        return ApiResponse.successResponse(Map.of("message", "Image deleted"));
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('SELLER')")
    public ApiResponse<ProductListResponse> getMyProducts(
            @RequestParam(value = "company_id", required = false) Long companyId,
            @RequestParam(value = "status", required = false) ProductModerationStatus status,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "per_page", defaultValue = "20") int perPage) {
        return ApiResponse.successResponse(productService.getMyProducts(companyId, status, page, perPage));
    }

    @GetMapping("/{slug}")
    @PreAuthorize("permitAll()")
    public ApiResponse<ProductDetailResponse> getBySlug(
            @PathVariable String slug,
            @RequestHeader(value = "X-SESSION-ID", required = false) String sessionId) {
        return ApiResponse.successResponse(productService.getPublicDetail(slug, sessionId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SELLER')")
    public ApiResponse<ProductResponse> update(@PathVariable Long id, @RequestBody @Valid UpdateProductRequest request) {
        return ApiResponse.successResponse(productService.update(id, request));
    }

    @PostMapping("/{id}/publish")
    @PreAuthorize("hasRole('SELLER')")
    public ApiResponse<Map<String, String>> publish(@PathVariable Long id) {
        productService.publish(id);
        return ApiResponse.successResponse(Map.of("message", "Product sent to moderation", "status", "pending"));
    }

    @PostMapping("/{id}/archive")
    @PreAuthorize("hasRole('SELLER')")
    public ApiResponse<Map<String, String>> archive(@PathVariable Long id) {
        productService.archive(id);
        return ApiResponse.successResponse(Map.of("message", "Product archived", "status", "archived"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SELLER','ADMIN','SUPER_ADMIN')")
    public ApiResponse<Map<String, String>> delete(@PathVariable Long id) {
        productService.delete(id);
        return ApiResponse.successResponse(Map.of("message", "Product deleted"));
    }
}
