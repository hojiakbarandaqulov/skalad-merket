package org.example.service;

import org.example.dto.ApiResponse;
import org.example.dto.CompanyRequestDTO;
import org.example.dto.CompanyResponseDTO;
import org.example.dto.CompanyShortDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CompanyService {

    ApiResponse<CompanyResponseDTO> create(CompanyRequestDTO company);

    ApiResponse<List<CompanyShortDTO>> getMyCompanies();

    ApiResponse<CompanyResponseDTO> getBySlug(String slug);

    ApiResponse<CompanyResponseDTO> update(Long id, CompanyRequestDTO dto);

    void submitVerification(Long id);

    void delete(Long id);

    String uploadLogo(Long id, MultipartFile file);
}
