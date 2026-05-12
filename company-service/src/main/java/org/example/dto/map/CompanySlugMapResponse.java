package org.example.dto.map;

import lombok.Getter;
import lombok.Setter;
import org.example.enums.VerificationStatus;

@Getter
@Setter
public class CompanySlugMapResponse {
    private Long id;
    private String name;
    private String slug;
    private VerificationStatus status;
    private Long regionId;
    private Long districtId;
    private String address;
    private String lat;
    private String lng;
}
