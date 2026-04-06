package org.example.service;

import org.example.dto.CompanyResponseDTO;
import org.example.dto.admin.ModerationDecisionRequest;
import org.example.dto.admin.ReasonRequest;
import org.example.enums.AppLanguage;
import org.example.enums.VerificationStatus;
import org.springframework.data.domain.PageImpl;

import java.util.List;

public interface AdminCompanyService {

    PageImpl<CompanyResponseDTO> getCompanies(VerificationStatus status, String q, int page, int perPage, AppLanguage language);

    List<CompanyResponseDTO> getModerationQueue();

    void verify(Long id, AppLanguage language);

    void reject(Long id, ModerationDecisionRequest request, AppLanguage language);

    void block(Long id, ReasonRequest request, AppLanguage language);
}
