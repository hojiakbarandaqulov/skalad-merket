package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.*;
import org.example.enums.AppLanguage;
import org.example.enums.LeadStatus;
import org.example.service.LeadService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/leads")
public class LeadController {
    private final LeadService leadService;

    @PostMapping
    public ApiResponse<LeadResponse> create(@Valid @RequestBody LeadCreateRequest request,
                                            @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language) {
        return ApiResponse.successResponse(leadService.create(request, language));
    }

    @GetMapping
    public ApiResponse<PagedResponse<LeadResponse>> buyerLeads(@RequestParam(required = false) LeadStatus status,
                                                               @RequestParam(defaultValue = "1") int page,
                                                               @RequestParam(defaultValue = "20") int perPage,
                                                               @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language) {
        return ApiResponse.successResponse(leadService.getBuyerLeads(status, page, perPage, language));
    }

    @GetMapping("/{id}")
    public ApiResponse<LeadResponse> getById(@PathVariable Long id,
                                             @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language) {
        return ApiResponse.successResponse(leadService.getById(id, language));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> cancel(@PathVariable Long id,
                                       @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language) {
        return ApiResponse.successResponse(leadService.cancel(id, language));
    }

    @GetMapping("/seller")
    public ApiResponse<PagedResponse<LeadResponse>> sellerLeads(@RequestParam(required = false) Long companyId,
                                                                @RequestParam(required = false) LeadStatus status,
                                                                @RequestParam(defaultValue = "1") int page,
                                                                @RequestParam(defaultValue = "20") int perPage,
                                                                @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language) {
        return ApiResponse.successResponse(leadService.getSellerLeads(companyId, status, page, perPage, language));
    }

    @PutMapping("/{id}/status")
    public ApiResponse<LeadResponse> updateStatus(@PathVariable Long id,
                                                  @Valid @RequestBody LeadStatusUpdateRequest request,
                                                  @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language) {
        return ApiResponse.successResponse(leadService.updateStatus(id, request, language));
    }
}
