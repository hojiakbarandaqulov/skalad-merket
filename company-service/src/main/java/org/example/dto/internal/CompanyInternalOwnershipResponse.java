package org.example.dto.internal;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CompanyInternalOwnershipResponse {
    private Long companyId;
    private boolean exists;
    private boolean owner;
    private boolean active;
}
