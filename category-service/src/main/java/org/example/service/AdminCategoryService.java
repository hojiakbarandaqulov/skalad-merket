package org.example.service;

import org.example.dto.CategoryAttributeCreateRequest;
import org.example.dto.CategoryResponse;
import org.example.dto.CategoryUpdateRequest;
import org.example.dto.categoryAtribute.CategoryAttributeResponse;
import org.example.dto.categoryAtribute.CategoryCreateRequest;
import org.example.enums.AppLanguage;

import java.util.List;

public interface AdminCategoryService {

    List<CategoryResponse> getCategories();

    CategoryResponse createCategory(CategoryCreateRequest request, AppLanguage language);

    CategoryResponse updateCategory(Long id, CategoryUpdateRequest request, AppLanguage language);

    void archiveCategory(Long id, AppLanguage language);

    CategoryAttributeResponse addAttribute(Long categoryId, CategoryAttributeCreateRequest request, AppLanguage language);

    CategoryAttributeResponse updateAttribute(Long categoryId, Long attrId, CategoryAttributeCreateRequest request, AppLanguage language);

    void deleteAttribute(Long categoryId, Long attrId, AppLanguage language);
}
