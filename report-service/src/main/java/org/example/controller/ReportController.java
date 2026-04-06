package org.example.controller;

import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.ApiResponse;
import org.example.dto.report.ReportCreateRequest;
import org.example.dto.report.ReportShortResponse;
import org.example.service.ReportService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {
    private final ReportService reportService;

    @PermitAll
    @PostMapping
    public ApiResponse<ReportShortResponse> createReport(@RequestBody @Valid ReportCreateRequest reportCreateRequest) {
        return ApiResponse.successResponse(reportService.createReport(reportCreateRequest));
    }
}
