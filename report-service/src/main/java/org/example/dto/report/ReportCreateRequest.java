package org.example.dto.report;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.enums.ReasonCode;
import org.example.enums.TargetType;

@Data
public class ReportCreateRequest {

    @NotNull(message = "targetType required")
    private TargetType targetType;

    @NotNull(message = "targetId required")
    private Long targetId;

    @NotNull(message = "reasonCode required")
    private ReasonCode reasonCode;

    private String comment;
}
