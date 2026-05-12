package org.example.client.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.enums.VerificationStatus;

@Getter
@Setter
public class CompanyMapSummaryResponse {
    private Long companyId;
    private String companyName;
    private String companyAddress;
    private String slug;
    private String lng;
    private String lat;
    private String logoUrl;
    private VerificationStatus verificationStatus;
}

