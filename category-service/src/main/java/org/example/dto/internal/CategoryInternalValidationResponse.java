package org.example.dto.internal;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryInternalValidationResponse {
    private Long categoryId;
    private boolean exists;
    private boolean active;
}
