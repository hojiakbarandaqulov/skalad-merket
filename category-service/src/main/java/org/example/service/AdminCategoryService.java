package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.CategoryAttributeCreateRequest;
import org.example.dto.CategoryResponse;
import org.example.dto.CategoryUpdateRequest;
import org.example.dto.categoryAtribute.CategoryAttributeResponse;
import org.example.dto.categoryAtribute.CategoryCreateRequest;
import org.example.entity.Category;
import org.example.entity.CategoryAttribute;
import org.example.exp.AppBadException;
import org.example.repository.CategoryAttributeRepository;
import org.example.repository.CategoryRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminCategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryAttributeRepository categoryAttributeRepository;

    public List<CategoryResponse> getCategories() {
        return categoryRepository.findAll(Sort.by(Sort.Direction.ASC, "sortOrder"))
                .stream()
                .map(this::toCategoryResponse)
                .toList();
    }

    @Transactional
    public CategoryResponse createCategory(CategoryCreateRequest request) {
        if (categoryRepository.existsBySlug(request.getSlug())) {
            throw new AppBadException("category.slug.exists");
        }

        Category category = new Category();
        applyCategoryFields(category, request);
        return toCategoryResponse(categoryRepository.save(category));
    }

    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryUpdateRequest request) {
        Category category = getCategory(id);
        if (!category.getSlug().equalsIgnoreCase(request.getSlug()) && categoryRepository.existsBySlug(request.getSlug())) {
            throw new AppBadException("category.slug.exists");
        }

        applyCategoryFields(category, request);
        return toCategoryResponse(categoryRepository.save(category));
    }

    @Transactional
    public void archiveCategory(Long id) {
        Category category = getCategory(id);
        category.setIsActive(Boolean.FALSE);
        categoryRepository.save(category);
    }

    @Transactional
    public CategoryAttributeResponse addAttribute(Long categoryId, CategoryAttributeCreateRequest request) {
        Category category = getCategory(categoryId);
        if (categoryAttributeRepository.existsByCategory_IdAndCodeIgnoreCase(categoryId, request.getCode())) {
            throw new AppBadException("category.attribute.code.exists");
        }

        CategoryAttribute attribute = new CategoryAttribute();
        attribute.setCategory(category);
        applyAttributeFields(attribute, request);
        return toAttributeResponse(categoryAttributeRepository.save(attribute));
    }

    @Transactional
    public CategoryAttributeResponse updateAttribute(Long categoryId, Long attrId, CategoryAttributeCreateRequest request) {
        CategoryAttribute attribute = categoryAttributeRepository.findByIdAndCategory_Id(attrId, categoryId)
                .orElseThrow(() -> new AppBadException("category.attribute.not.found"));

        if (!attribute.getCode().equalsIgnoreCase(request.getCode())
                && categoryAttributeRepository.existsByCategory_IdAndCodeIgnoreCase(categoryId, request.getCode())) {
            throw new AppBadException("category.attribute.code.exists");
        }

        applyAttributeFields(attribute, request);
        return toAttributeResponse(categoryAttributeRepository.save(attribute));
    }

    @Transactional
    public void deleteAttribute(Long categoryId, Long attrId) {
        CategoryAttribute attribute = categoryAttributeRepository.findByIdAndCategory_Id(attrId, categoryId)
                .orElseThrow(() -> new AppBadException("category.attribute.not.found"));
        categoryAttributeRepository.delete(attribute);
    }

    private Category getCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new AppBadException("category.not.found"));
    }

    private void applyCategoryFields(Category category, CategoryCreateRequest request) {
        category.setParent(resolveParent(request.getParentId(), category.getId()));
        category.setNameUz(request.getNameUz());
        category.setNameRu(request.getNameRu());
        category.setNameEn(request.getNameEn());
        category.setSlug(request.getSlug());
        category.setIcon(request.getIcon());
        category.setSortOrder(request.getSortOrder());
        category.setIsActive(request.getIsActive());
    }

    private void applyCategoryFields(Category category, CategoryUpdateRequest request) {
        category.setParent(resolveParent(request.getParentId(), category.getId()));
        category.setNameUz(request.getNameUz());
        category.setNameRu(request.getNameRu());
        category.setNameEn(request.getNameEn());
        category.setSlug(request.getSlug());
        category.setIcon(request.getIcon());
        category.setSortOrder(request.getSortOrder());
        category.setIsActive(request.getIsActive());
    }

    private Category resolveParent(Long parentId, Long currentId) {
        if (parentId == null || parentId == 0) {
            return null;
        }
        if (currentId != null && currentId.equals(parentId)) {
            throw new AppBadException("category.cannot.be.own.parent");
        }
        return categoryRepository.findById(parentId)
                .orElseThrow(() -> new AppBadException("category.parent.not.found"));
    }

    private void applyAttributeFields(CategoryAttribute attribute, CategoryAttributeCreateRequest request) {
        attribute.setCode(request.getCode());
        attribute.setLabel(request.getLabel());
        attribute.setDataType(request.getDataType());
        attribute.setIsRequired(request.getIsRequired());
        attribute.setIsFilterable(request.getIsFilterable());
        attribute.setOptionsJson(request.getOptionsJson());
        attribute.setSortOrder(request.getSortOrder());
    }

    private CategoryResponse toCategoryResponse(Category category) {
        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setNameUz(category.getNameUz());
        response.setNameRu(category.getNameRu());
        response.setNameEn(category.getNameEn());
        response.setSlug(category.getSlug());
        response.setIcon(category.getIcon());
        response.setSortOrder(category.getSortOrder());
        response.setIsActive(category.getIsActive());
        return response;
    }

    private CategoryAttributeResponse toAttributeResponse(CategoryAttribute attribute) {
        CategoryAttributeResponse response = new CategoryAttributeResponse();
        response.setId(attribute.getId());
        response.setCode(attribute.getCode());
        response.setLabel(attribute.getLabel());
        response.setDataType(attribute.getDataType());
        response.setIsRequired(attribute.getIsRequired());
        response.setIsFilterable(attribute.getIsFilterable());
        response.setOptionsJson(attribute.getOptionsJson());
        response.setSortOrder(attribute.getSortOrder());
        return response;
    }
}
