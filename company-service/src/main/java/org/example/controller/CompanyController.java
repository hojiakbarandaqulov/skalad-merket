package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.ApiResponse;
import org.example.dto.CompanyRequestDTO;
import org.example.dto.CompanyResponseDTO;
import org.example.service.CompanyService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/companies")
public class CompanyController {
    private final CompanyService companyService;

    @PreAuthorize("hasAnyRole('ROLE_SELLER','ROLE_BUYER')")
    @PostMapping("create")
    public ApiResponse<CompanyResponseDTO> createCompany(@RequestBody @Valid CompanyRequestDTO company) {
        return companyService.create(company);
    }
}
