package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.clent.ProductClient;
import org.example.dto.*;
import org.example.entity.Lead;
import org.example.entity.LeadItem;
import org.example.enums.AppLanguage;
import org.example.enums.LeadSource;
import org.example.enums.LeadStatus;
import org.example.exp.AppBadException;
import org.example.repository.LeadItemRepository;
import org.example.repository.LeadRepository;
import org.example.service.LeadService;
import org.example.service.ResourceBundleService;
import org.example.utils.SpringSecurityUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeadServiceImpl implements LeadService {
    private final LeadRepository leadRepository;
    private final LeadItemRepository leadItemRepository;
    private final ResourceBundleService messageService;
    private final ProductClient client;

    @Override
    public LeadResponse create(LeadCreateRequest request, AppLanguage language) {
        Long buyerId = requireProfileId(language);
        List<Long> productIds = request.getSource() == LeadSource.CART ? request.getProductIds() : List.of(request.getProductId());
        if (productIds == null || productIds.isEmpty() || productIds.get(0) == null) {
            throw new AppBadException(messageService.getMessage("lead.items.required", language));
        }
        List<ProductResponse> products = productIds.stream()
                .map(client::getById)
                .toList();

        Lead lead = new Lead();
        lead.setBuyerId(buyerId);
        lead.setSellerId(products.get(0).getSellerId());
        lead.setCompanyId(products.get(0).getCompanyId());
        lead.setSource(request.getSource());
        lead.setContactName(request.getContactName());
        lead.setContactPhone(request.getContactPhone());
        lead.setComment(request.getComment());
        Lead savedLead = leadRepository.save(lead);

        for (ProductResponse product : products) {
            LeadItem item = new LeadItem();
            item.setLeadId(savedLead.getId());
            item.setProductId(product.getId());
            item.setProductNameSnapshot(product.getName());
            item.setPriceSnapshot(product.getPrice());
            item.setQuantity(1);
            leadItemRepository.save(item);
        }
        return toResponse(savedLead);
    }

    @Override
    public PagedResponse<LeadResponse> getBuyerLeads(LeadStatus status, int page, int perPage, AppLanguage language) {
        Long buyerId = requireProfileId(language);
        Specification<Lead> spec = (root, query, cb) -> cb.and(cb.equal(root.get("buyerId"), buyerId), cb.isFalse(root.get("deleted")));
        if (status != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
        }
        Page<Lead> leads = leadRepository.findAll(spec, PageRequest.of(Math.max(page - 1, 0), perPage));
        return ServiceHelper.toPagedResponse(leads.map(this::toResponse));
    }

    @Override
    public PagedResponse<LeadResponse> getSellerLeads(Long companyId, LeadStatus status, int page, int perPage, AppLanguage language) {
        Long sellerId = requireProfileId(language);
        Specification<Lead> spec = (root, query, cb) -> cb.and(cb.equal(root.get("sellerId"), sellerId), cb.isFalse(root.get("deleted")));
        if (companyId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("companyId"), companyId));
        }
        if (status != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
        }
        Page<Lead> leads = leadRepository.findAll(spec, PageRequest.of(Math.max(page - 1, 0), perPage));
        return ServiceHelper.toPagedResponse(leads.map(this::toResponse));
    }

    @Override
    public LeadResponse getById(Long id, AppLanguage language) {
        Lead lead = findLead(id, language);
        Long profileId = requireProfileId(language);
        if (!profileId.equals(lead.getBuyerId()) && !profileId.equals(lead.getSellerId())) {
            throw new AppBadException(messageService.getMessage("lead.forbidden", language));
        }
        return toResponse(lead);
    }

    @Override
    public Boolean cancel(Long id, AppLanguage language) {
        Lead lead = findLead(id, language);
        if (!requireProfileId(language).equals(lead.getBuyerId())) {
            throw new AppBadException(messageService.getMessage("lead.forbidden", language));
        }
        lead.setStatus(LeadStatus.CANCELED);
        leadRepository.save(lead);
        return true;
    }

    @Override
    public LeadResponse updateStatus(Long id, LeadStatusUpdateRequest request, AppLanguage language) {
        Lead lead = findLead(id, language);
        if (!requireProfileId(language).equals(lead.getSellerId())) {
            throw new AppBadException(messageService.getMessage("lead.forbidden", language));
        }
        lead.setStatus(request.getStatus());
        lead.setCloseReason(request.getCloseReason());
        return toResponse(leadRepository.save(lead));
    }

    private Lead findLead(Long id, AppLanguage language) {
        return leadRepository.findById(id)
                .filter(item -> Boolean.FALSE.equals(item.getDeleted()))
                .orElseThrow(() -> new AppBadException(messageService.getMessage("lead.not.found", language)));
    }

    private LeadResponse toResponse(Lead lead) {
        return LeadResponse.builder()
                .id(lead.getId())
                .buyerId(lead.getBuyerId())
                .sellerId(lead.getSellerId())
                .companyId(lead.getCompanyId())
                .source(lead.getSource())
                .status(lead.getStatus())
                .contactName(lead.getContactName())
                .contactPhone(lead.getContactPhone())
                .comment(lead.getComment())
                .closeReason(lead.getCloseReason())
                .items(leadItemRepository.findByLeadIdAndDeletedFalse(lead.getId()).stream()
                        .map(item -> LeadItemResponse.builder()
                                .productId(item.getProductId())
                                .productNameSnapshot(item.getProductNameSnapshot())
                                .priceSnapshot(item.getPriceSnapshot())
                                .quantity(item.getQuantity())
                                .build())
                        .toList())
                .build();
    }

    private Long requireProfileId(AppLanguage language) {
        Long profileId = SpringSecurityUtil.getProfileId();
        if (profileId == null) {
            throw new AppBadException(messageService.getMessage("user.not.found", language));
        }
        return profileId;
    }
}
