package org.example.service;

import org.example.dto.ApiResponse;
import org.example.dto.CompanyRequestDTO;
import org.example.dto.CompanyResponseDTO;

public interface CompanyService {

    ApiResponse<CompanyResponseDTO> create(CompanyRequestDTO company);
}
