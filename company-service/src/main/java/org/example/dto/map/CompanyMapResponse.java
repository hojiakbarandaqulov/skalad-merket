package org.example.dto.map;

import lombok.Getter;
import lombok.Setter;
import org.example.enums.VerificationStatus;

@Getter
@Setter
public class CompanyMapResponse {
    private Long companyId;
    private String companyName;
    private String companyAddress;
    private String slug;
    private String lng;
    private String lat;
    private String logoUrl;
    private VerificationStatus verificationStatus;
}
