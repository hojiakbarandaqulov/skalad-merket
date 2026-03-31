package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.shaded.com.google.protobuf.Api;
import org.example.dto.ApiResponse;
import org.example.dto.CompanyRequestDTO;
import org.example.dto.CompanyResponseDTO;
import org.example.dto.CompanyShortDTO;
import org.example.service.CompanyService;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/companies")
public class CompanyController {
    private final CompanyService companyService;

    @PostMapping("create")
    public ApiResponse<CompanyResponseDTO> createCompany(@RequestBody @Valid CompanyRequestDTO company) {
        return companyService.create(company);
    }

    @GetMapping
    @PreAuthorize("hasRole('SELLER')")
    public ApiResponse<List<CompanyShortDTO>> getMyCompanies() {
        return companyService.getMyCompanies();
    }

    @GetMapping("/{slug}")
    public ApiResponse<CompanyResponseDTO> getBySlug(
            @PathVariable String slug) {
        return companyService.getBySlug(slug);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SELLER')")
    public ApiResponse<CompanyResponseDTO> update(
            @PathVariable Long id,
            @RequestBody @Valid CompanyRequestDTO dto) {
        return companyService.update(id, dto);
    }

    @PostMapping(value = "/{id}/logo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('SELLER')")
    public ApiResponse<String> uploadLogo(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        String logoUrl = companyService.uploadLogo(id, file);
        return ApiResponse.successResponse(logoUrl);
    }

    @PostMapping("/{id}/submit-verification")
    @PreAuthorize("hasRole('SELLER')")
    public ApiResponse<Map<String, String>> submitVerification(
            @PathVariable Long id) {
        companyService.submitVerification(id);
        return ApiResponse.successResponse(Map.of(
                "message", "Moderatsiyaga yuborildi",
                "status", "PENDING_VERIFICATION"
        ));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SELLER')")
    public ApiResponse<Boolean> delete(@PathVariable Long id) {
        companyService.delete(id);
        return ApiResponse.successResponse(true);
    }


}
