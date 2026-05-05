package org.example.service;

import org.example.dto.*;
import org.example.dto.map.CompanyMapResponse;
import org.example.dto.map.CompanySlugMapResponse;
import org.example.enums.AppLanguage;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CompanyService {

    ApiResponse<CompanyResponseDTO> create(CompanyRequestDTO company, AppLanguage language);

    ApiResponse<List<CompanyShortDTO>> getMyCompanies(AppLanguage language);

    ApiResponse<PageImpl<CompanyShortDTO>> getPublicCompanies(int page, int perPage, AppLanguage language);

    ApiResponse<PageImpl<CompanyShortDTO>> search(String q, Boolean verified, Long category, Long regionId, int page, int perPage, AppLanguage language);

    ApiResponse<CompanySlugMapResponse> getBySlug(String slug, AppLanguage language);

    ApiResponse<PageImpl<CompanyProductResponse>> getCompanyProducts(String slug, int page, int perPage, AppLanguage language);

    ApiResponse<CompanyResponseDTO> update(Long id, CompanyRequestDTO dto, AppLanguage language);

    ApiResponse<CompanyDocumentResponse> addDocument(Long id, CompanyDocumentCreateRequest request, AppLanguage language);

    void submitVerification(Long id, AppLanguage language);

    void delete(Long id, AppLanguage language);

    UploadDTO uploadLogo(Long id, MultipartFile file, AppLanguage language);

    UploadDTO uploadCoverUrl(Long companyId, MultipartFile file, AppLanguage language);

    PageImpl<CompanyMapResponse> getMapCompany(Long regionId, String q, Boolean verified, int page, int perPage, AppLanguage language);
}
