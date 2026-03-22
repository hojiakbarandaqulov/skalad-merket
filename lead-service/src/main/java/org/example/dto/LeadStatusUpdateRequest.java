package org.example.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.example.enums.LeadStatus;

@Getter
@Setter
public class LeadStatusUpdateRequest {
    @NotNull
    private LeadStatus status;
    private String closeReason;
}
