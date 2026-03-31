package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.ApiResponse;
import org.example.dto.CategoryAttributeCreateRequest;
import org.example.dto.CategoryResponse;
import org.example.dto.CategoryUpdateRequest;
import org.example.dto.categoryAtribute.CategoryAttributeResponse;
import org.example.dto.categoryAtribute.CategoryCreateRequest;
import org.example.service.AdminCategoryService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/categories")
public class AdminCategoryController {

    private final AdminCategoryService adminCategoryService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<List<CategoryResponse>> getCategories() {
        return ApiResponse.successResponse(adminCategoryService.getCategories());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<CategoryResponse> createCategory(@RequestBody @Valid CategoryCreateRequest request) {
        return ApiResponse.successResponse(adminCategoryService.createCategory(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<CategoryResponse> updateCategory(@PathVariable Long id,
                                                        @RequestBody @Valid CategoryUpdateRequest request) {
        return ApiResponse.successResponse(adminCategoryService.updateCategory(id, request));
    }

    @PutMapping("/{id}/archive")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<Map<String, String>> archiveCategory(@PathVariable Long id) {
        adminCategoryService.archiveCategory(id);
        return ApiResponse.successResponse(Map.of("message", "Category archived", "archived", Boolean.TRUE.toString()));
    }

    @PostMapping("/{id}/attributes")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<CategoryAttributeResponse> addAttribute(@PathVariable Long id,
                                                               @RequestBody @Valid CategoryAttributeCreateRequest request) {
        return ApiResponse.successResponse(adminCategoryService.addAttribute(id, request));
    }

    @PutMapping("/{id}/attributes/{attrId}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<CategoryAttributeResponse> updateAttribute(@PathVariable Long id,
                                                                  @PathVariable Long attrId,
                                                                  @RequestBody @Valid CategoryAttributeCreateRequest request) {
        return ApiResponse.successResponse(adminCategoryService.updateAttribute(id, attrId, request));
    }

    @DeleteMapping("/{id}/attributes/{attrId}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public ApiResponse<Map<String, String>> deleteAttribute(@PathVariable Long id, @PathVariable Long attrId) {
        adminCategoryService.deleteAttribute(id, attrId);
        return ApiResponse.successResponse(Map.of("message", "Category attribute deleted", "deleted", Boolean.TRUE.toString()));
    }
}
