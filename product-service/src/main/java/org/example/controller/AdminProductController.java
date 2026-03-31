package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.ApiResponse;
import org.example.dto.admin.ModerationDecisionRequest;
import org.example.dto.admin.PromotionRequest;
import org.example.dto.admin.ReasonRequest;
import org.example.dto.product.ProductListResponse;
import org.example.dto.product.ProductResponse;
import org.example.enums.ProductModerationStatus;
import org.example.service.AdminProductService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/products")
public class AdminProductController {

    private final AdminProductService adminProductService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<ProductListResponse> getProducts(
            @RequestParam(required = false) ProductModerationStatus status,
            @RequestParam(value = "company_id", required = false) Long companyId,
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(value = "per_page", defaultValue = "20") int perPage) {
        return ApiResponse.successResponse(adminProductService.getProducts(status, companyId, q, page, perPage));
    }

    @GetMapping("/moderation-queue")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<List<ProductResponse>> getModerationQueue() {
        return ApiResponse.successResponse(adminProductService.getModerationQueue());
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<Map<String, String>> approve(@PathVariable Long id) {
        adminProductService.approve(id);
        return ApiResponse.successResponse(Map.of("message", "Product approved", "status", ProductModerationStatus.APPROVED.name()));
    }

    @PutMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<Map<String, String>> reject(@PathVariable Long id,
                                                   @RequestBody(required = false) ModerationDecisionRequest request) {
        adminProductService.reject(id, request);
        return ApiResponse.successResponse(Map.of("message", "Product rejected", "status", ProductModerationStatus.REJECTED.name()));
    }

    @PutMapping("/{id}/block")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<Map<String, String>> block(@PathVariable Long id,
                                                  @RequestBody(required = false) ReasonRequest request) {
        adminProductService.block(id, request);
        return ApiResponse.successResponse(Map.of("message", "Product blocked", "blocked", Boolean.TRUE.toString()));
    }

    @PutMapping("/{id}/promote")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<Map<String, String>> promote(@PathVariable Long id,
                                                    @RequestBody(required = false) PromotionRequest request) {
        adminProductService.promote(id, request);
        return ApiResponse.successResponse(Map.of("message", "Product promoted", "promoted", Boolean.TRUE.toString()));
    }
}
