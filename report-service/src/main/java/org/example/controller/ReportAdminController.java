package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.ApiResponse;
import org.example.dto.report.ReportInfoResponse;
import org.example.dto.report.ReportResolveRequest;
import org.example.dto.report.ReportResolveResponse;
import org.example.dto.report.ReportResponse;
import org.example.enums.AppLanguage;
import org.example.enums.ReportStatus;
import org.example.enums.TargetType;
import org.example.service.ReportService;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/admin/reports")
public class ReportAdminController {
    private final ReportService reportService;

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @GetMapping
    public ApiResponse<PageImpl<ReportResponse>> getReport(
            @RequestParam(required = false) ReportStatus status,
            @RequestParam(required = false) TargetType targetType,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language
    ) {
        return ApiResponse.successResponse(reportService.getReport(status, targetType, page, size, language));
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @GetMapping("/{id}")
    public ApiResponse<ReportInfoResponse> getByReport(
            @PathVariable Long id,
            @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language
    ) {
        return ApiResponse.successResponse(reportService.getByReport(id, language));
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @PutMapping("/{id}/resolve")
    public ApiResponse<ReportResolveResponse> reportResolve(
            @PathVariable Long id,
            @RequestBody ReportResolveRequest request,
            @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language
    ) {
        return ApiResponse.successResponse(reportService.reportResolve(id,request, language));
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @PutMapping("/{id}/reject")
    public ApiResponse<ReportResolveResponse> reportReject(
            @PathVariable Long id,
            @RequestBody ReportResolveRequest request,
            @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language
    ) {
        return ApiResponse.successResponse(reportService.reportReject(id, request,language));
    }
}
