package org.example.service;

import org.example.dto.ApiResponse;
import org.example.dto.report.*;
import org.example.enums.AppLanguage;
import org.example.enums.ReportStatus;
import org.example.enums.TargetType;
import org.springframework.data.domain.PageImpl;

public interface ReportService {

    ReportShortResponse createReport(ReportCreateRequest reportCreateRequest);

    PageImpl<ReportResponse> getReport(ReportStatus status, TargetType targetType, int page, int size, AppLanguage language);

    ReportInfoResponse getByReport(Long id, AppLanguage language);

    ReportResolveResponse reportResolve(Long id, ReportResolveRequest request, AppLanguage language);

    ReportResolveResponse reportReject(Long id, ReportResolveRequest request, AppLanguage language);
}
