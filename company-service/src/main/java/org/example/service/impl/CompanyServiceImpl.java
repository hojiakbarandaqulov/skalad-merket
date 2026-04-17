package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.ApiResponse;
import org.example.dto.CompanyRequestDTO;
import org.example.dto.CompanyResponseDTO;
import org.example.dto.CompanyShortDTO;
import org.example.dto.UploadDTO;
import org.example.dto.kafka.UserRoleUpdateEvent;
import org.example.entity.Company;
import org.example.enums.AppLanguage;
import org.example.enums.VerificationStatus;
import org.example.exp.AppBadException;
import org.example.repository.CompanyRepository;
import org.example.service.CompanyService;
import org.example.service.KafkaProducerService;
import org.example.service.ResourceBundleService;
import org.example.utils.SpringSecurityUtil;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;
    private final KafkaProducerService kafkaProducerService;
    private final ModelMapper modelMapper;
    private final RestTemplate restTemplate;
    private final ResourceBundleService messageService;

    private static final int MAX_COMPANIES_PER_SELLER = 5;

    @Override
    public ApiResponse<CompanyResponseDTO> create(CompanyRequestDTO requestDTO, AppLanguage language) {
        Long userId = SpringSecurityUtil.getProfileId();
        long count = companyRepository.countByOwnerUserIdAndDeletedAtIsNull(userId);
        if (count >= MAX_COMPANIES_PER_SELLER) {
            throw new AppBadException(messageService.getMessage("maximum.of.5.companies.can.be.created", language));
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

    @Override
    public ApiResponse<List<CompanyShortDTO>> getMyCompanies(AppLanguage language) {
        Long userId = SpringSecurityUtil.getProfileId();
        Optional<Company> company = companyRepository.findByOwnerUserIdAndDeletedFalse(userId);
        if (company.isEmpty()) {
            throw new AppBadException(messageService.getMessage("company.not.found", language));
        }
        List<CompanyShortDTO> companyShortDTO = Collections.singletonList(modelMapper.map(company.get(), CompanyShortDTO.class));
        return ApiResponse.successResponse(companyShortDTO);
    }

    @Override
    public ApiResponse<CompanyResponseDTO> getBySlug(String slug, AppLanguage language) {
        Company company = companyRepository.findBySlugAndDeletedAtIsNullAndVerificationStatusIn(
                slug,
                List.of(VerificationStatus.VERIFIED, VerificationStatus.PENDING_VERIFICATION)
        );
        if (company == null) {
            throw new AppBadException(messageService.getMessage("company.not.found", language));
        }
        CompanyResponseDTO responseDTO = modelMapper.map(company, CompanyResponseDTO.class);
        return ApiResponse.successResponse(responseDTO);
    }

    @Transactional
    @Override
    public ApiResponse<CompanyResponseDTO> update(Long id, CompanyRequestDTO dto, AppLanguage language) {
        Long profileId = SpringSecurityUtil.getProfileId();
        Company company = findOwnedCompany(id, profileId, language);
        company.setName(dto.getName());
        company.setShortDescription(dto.getShortDescription());
        company.setDescription(dto.getDescription());
        company.setStir(dto.getStir());
        company.setPhonePrimary(dto.getPhonePrimary());
        company.setPhoneSecondary(dto.getPhoneSecondary());
        company.setWebsite(dto.getWebsite());
        company.setRegionId(dto.getRegionId());
        company.setDistrictId(dto.getDistrictId());
        company.setAddress(dto.getAddress());
        Company saved = companyRepository.save(company);
        CompanyResponseDTO map = modelMapper.map(saved, CompanyResponseDTO.class);
        return ApiResponse.successResponse(map);
    }

    @Override
    public void submitVerification(Long id, AppLanguage language) {
        Long profileId = SpringSecurityUtil.getProfileId();
        Company company = findOwnedCompany(id, profileId, language);
        if (!company.getVerificationStatus().equals(VerificationStatus.DRAFT)) {
            throw new AppBadException(messageService.getMessage("company.verification.failed", language));
        }
        company.setVerificationStatus(VerificationStatus.PENDING_VERIFICATION);
        companyRepository.save(company);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(Long id, AppLanguage language) {
        Long profileId = SpringSecurityUtil.getProfileId();
        Company company = findOwnedCompany(id, profileId, language);
        company.setDeletedAt(LocalDateTime.now());
        company.setDeleted(true);
        companyRepository.save(company);
    }

    @Override
    public String uploadLogo(Long id, MultipartFile file, AppLanguage language) {
        Long profileId = SpringSecurityUtil.getProfileId();
        Company company = findOwnedCompany(id, profileId, language);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", file.getResource());
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<UploadDTO> response = restTemplate.postForEntity(
                "http://localhost:8082/api/v1/attach/upload",
                requestEntity,
                UploadDTO.class
        );
        UploadDTO uploadDTO = response.getBody();
        if (uploadDTO == null) {
            throw new AppBadException(messageService.getMessage("logo.not.download", language));
        }
        company.setLogoPath(uploadDTO.getUrl());
        companyRepository.save(company);
        return company.getLogoPath();
    }

    private Company findOwnedCompany(Long id, Long profileId, AppLanguage language) {
        return companyRepository.findByIdAndOwnerUserIdAndDeletedAtIsNull(id, profileId)
                .orElseThrow(() -> new AppBadException(messageService.getMessage("company.not.found", language)));
    }

    private String generateSlug(String name) {
        return name.toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-|-$", "");
    }
}
