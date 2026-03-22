package org.example.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.enums.LeadSource;
import org.example.enums.LeadStatus;

import java.util.List;

@Getter
@Setter
@Builder
public class LeadResponse {
    private Long id;
    private Long buyerId;
    private Long sellerId;
    private Long companyId;
    private LeadSource source;
    private LeadStatus status;
    private String contactName;
    private String contactPhone;
    private String comment;
    private String closeReason;
    private List<LeadItemResponse> items;
}
