package org.example.dto.internal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyInternalSummaryResponse {
    private Long id;
    private String name;
    private String slug;
    private String logoPath;
}
