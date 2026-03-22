package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.internal.CompanyInternalOwnershipResponse;
import org.example.dto.internal.CompanyInternalSummaryResponse;
import org.example.entity.Company;
import org.example.enums.VerificationStatus;
import org.example.exp.AppBadException;
import org.example.repository.CompanyRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/companies")
public class CompanyInternalController {

    private final CompanyRepository companyRepository;

    @GetMapping("/{companyId}/ownership-check")
    public CompanyInternalOwnershipResponse ownershipCheck(@PathVariable Long companyId, @RequestParam Long sellerId) {
        Company company = companyRepository.findByIdAndDeletedAtIsNull(companyId).orElse(null);
        if (company == null) {
            return CompanyInternalOwnershipResponse.builder()
                    .companyId(companyId)
                    .exists(false)
                    .owner(false)
                    .active(false)
                    .build();
        }

        boolean active = !Boolean.TRUE.equals(company.getIsBlocked())
                && company.getDeletedAt() == null
                && company.getVerificationStatus() == VerificationStatus.VERIFIED;

        return CompanyInternalOwnershipResponse.builder()
                .companyId(companyId)
                .exists(true)
                .owner(company.getOwnerUserId().equals(sellerId))
                .active(active)
                .build();
    }

    @GetMapping("/owned")
    public List<Long> ownedCompanies(@RequestParam Long sellerId) {
        return companyRepository.findAllByOwnerUserIdAndDeletedAtIsNull(sellerId)
                .stream()
                .map(Company::getId)
                .toList();
    }

    @GetMapping("/{companyId}/summary")
    public CompanyInternalSummaryResponse summary(@PathVariable Long companyId) {
        Company company = companyRepository.findByIdAndDeletedAtIsNull(companyId)
                .orElseThrow(() -> new AppBadException("company.not.found"));

        return CompanyInternalSummaryResponse.builder()
                .id(company.getId())
                .name(company.getName())
                .slug(company.getSlug())
                .logoPath(company.getLogoPath())
                .build();
    }
}
