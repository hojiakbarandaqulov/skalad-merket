package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.example.enums.LeadSource;

import java.util.List;

@Getter
@Setter
public class LeadCreateRequest {
    @NotNull
    private LeadSource source;
    private List<Long> productIds;
    private Long productId;
    @NotBlank
    private String contactName;
    @NotBlank
    private String contactPhone;
    private String comment;
}
