package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.example.enums.LeadSource;

import jakarta.validation.constraints.Min;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class LeadCreateRequest {
    @NotNull
    private LeadSource source;
    private List<Long> productIds;
    private Long productId;
    @Min(1)
    private Integer quantity;
    @NotBlank
    private String contactName;
    @NotBlank
    private String contactPhone;
    private String contactEmail;
    private String deliveryAddress;
    private LocalDate neededDate;
    private String comment;
}
