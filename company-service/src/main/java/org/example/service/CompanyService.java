package org.example.service;

import org.example.dto.*;
import org.example.enums.AppLanguage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CompanyService {

    ApiResponse<CompanyResponseDTO> create(CompanyRequestDTO company, AppLanguage language);

    ApiResponse<List<CompanyShortDTO>> getMyCompanies(AppLanguage language);

    ApiResponse<CompanyResponseDTO> getBySlug(String slug, AppLanguage language);

    ApiResponse<CompanyResponseDTO> update(Long id, CompanyRequestDTO dto, AppLanguage language);

    void submitVerification(Long id, AppLanguage language);

    void delete(Long id, AppLanguage language);

    UploadDTO uploadLogo(Long id, MultipartFile file, AppLanguage language);

    UploadDTO uploadCoverUrl(Long companyId, MultipartFile file, AppLanguage language);
}
