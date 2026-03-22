package org.example.dto.internal;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CompanyInternalSummaryResponse {
    private Long id;
    private String name;
    private String slug;
    private String logoPath;
}
