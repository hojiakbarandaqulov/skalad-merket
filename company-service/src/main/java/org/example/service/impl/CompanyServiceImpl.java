package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.ApiResponse;
import org.example.dto.CompanyRequestDTO;
import org.example.dto.CompanyResponseDTO;
import org.example.entity.Company;
import org.example.enums.VerificationStatus;
import org.example.exp.AppBadException;
import org.example.repository.CompanyRepository;
import org.example.service.CompanyService;
import org.example.utils.SpringSecurityUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;

    private final ModelMapper modelMapper;

    private static final int MAX_COMPANIES_PER_SELLER = 5;

    @Override
    public ApiResponse<CompanyResponseDTO> create(CompanyRequestDTO requestDTO) {
        Long userId= SpringSecurityUtil.getProfileId();
        long count = companyRepository.countByOwnerUserIdAndDeletedAtIsNull(userId);
        if (count >= MAX_COMPANIES_PER_SELLER) {
            throw new AppBadException("Maksimal 5 ta kompaniya yaratish mumkin");
        }
        Company companyMap = modelMapper.map(requestDTO, Company.class);

        companyMap.setOwnerUserId(userId);
        companyMap.setSlug(generateSlug(requestDTO.getName()));
        companyMap.setVerificationStatus(VerificationStatus.DRAFT);
        companyMap.setIsBlocked(false);

        Company saved = companyRepository.save(companyMap);
        CompanyResponseDTO responseDTO = modelMapper.map(saved, CompanyResponseDTO.class);
        return ApiResponse.successResponse(responseDTO);
    }


    private String generateSlug(String name) {
        return name.toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-|-$", "");
    }
}
