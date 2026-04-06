package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.ApiResponse;
import org.example.dto.report.*;
import org.example.entity.Report;
import org.example.enums.AppLanguage;
import org.example.enums.ReportStatus;
import org.example.enums.TargetType;
import org.example.exp.AppBadException;
import org.example.repository.ReportRepository;
import org.example.service.ReportService;
import org.example.service.ResourceBundleService;
import org.example.utils.SpringSecurityUtil;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class ReportServiceImpl implements ReportService {
    private final ReportRepository repository;
    private final ResourceBundleService messageService;
    private final ModelMapper modelMapper;

    @Override
    public ReportShortResponse createReport(ReportCreateRequest reportCreateRequest) {
        Long profileId = SpringSecurityUtil.getProfileId();
        Report report = new Report();
        report.setReporterUserId(profileId);
        report.setTargetType(reportCreateRequest.getTargetType());
        report.setTargetId(reportCreateRequest.getTargetId());
        report.setReasonCode(reportCreateRequest.getReasonCode());
        report.setComment(reportCreateRequest.getComment());
        repository.save(report);
        return new ReportShortResponse(report.getId(),report.getStatus());
    }

    @Override
    public PageImpl<ReportResponse> getReport(ReportStatus status, TargetType targetType, int page, int size, AppLanguage language) {
        int page1 = normalizePage(page, language);
        int size1 = normalizePerPage(size, language);

        Specification<Report> spec = Specification.where(null);
        if (status != null) {
            spec=spec.and((root,query,cb)->cb.equal(root.get("status"), status));
        }
        if (targetType != null) {
            spec=spec.and((root,query,cb)->cb.equal(root.get("targetType"), targetType));
        }
        PageRequest pageRequest = PageRequest.of(page1 - 1, size1, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<Report> result = repository.findAll(spec, pageRequest);
        List<ReportResponse> responses = result.getContent().stream()
                .map(this::toResponse).toList();

        return new PageImpl<>(responses,pageRequest,result.getTotalElements());
    }

    @Override
    public ReportInfoResponse getByReport(Long id, AppLanguage language) {
        Optional<Report> optionalReport=repository.findByIdAndDeletedFalse(id);
        if (optionalReport.isPresent()) {
            Report report=optionalReport.get();
            return modelMapper.map(report, ReportInfoResponse.class);
        }
        log.info("report not found id={}",id);
        throw new AppBadException(messageService.getMessage("report.not.found",language));
    }


    @Override
    public ReportResolveResponse reportReject(Long id, ReportResolveRequest request, AppLanguage language) {
        Long profileId = SpringSecurityUtil.getProfileId();
        Optional<Report> optionalReport=repository.findByIdAndDeletedFalse(id);
        if (optionalReport.isPresent()) {
            Report report = optionalReport.get();
            report.setStatus(ReportStatus.REJECTED);
            report.setResolvedBy(profileId);
            report.setResolvedAt(LocalDateTime.now());
            report.setResolutionNote(request.getResolutionNote());
            repository.save(report);
            return modelMapper.map(report, ReportResolveResponse.class);
        }
        log.info("report not found reportId={}",id);
        throw new AppBadException(messageService.getMessage("report.not.found",language));
    }

    @Override
    public ReportResolveResponse reportResolve(Long id,ReportResolveRequest request, AppLanguage language) {
        Long profileId = SpringSecurityUtil.getProfileId();
        Optional<Report> optionalReport=repository.findByIdAndDeletedFalse(id);
        if (optionalReport.isPresent()) {
            Report report = optionalReport.get();
            report.setStatus(ReportStatus.RESOLVED);
            report.setResolvedBy(profileId);
            report.setResolvedAt(LocalDateTime.now());
            report.setResolutionNote(request.getResolutionNote());
            repository.save(report);
            return modelMapper.map(report, ReportResolveResponse.class);
        }
        log.info("report not found");
        throw new AppBadException(messageService.getMessage("report.not.found",language));
    }

    private ReportResponse toResponse(Report report) {
        ReportResponse response = new ReportResponse();
        response.setId(report.getId());
        response.setTargetType(report.getTargetType());
        response.setTargetId(report.getTargetId());
        response.setReasonCode(report.getReasonCode());
        response.setStatus(report.getStatus());
        response.setCreatedDate(report.getCreatedDate());
        return response;
    }


    private int normalizePage(int page, AppLanguage language) {
        if (page < 1) {
            throw new AppBadException(messageService.getMessage("page.invalid", language));
        }
        return page;
    }

    private int normalizePerPage(int perPage, AppLanguage language) {
        if (perPage < 1 || perPage > 100) {
            throw new AppBadException(messageService.getMessage("per.page.invalid", language));
        }
        return perPage;
    }
}
