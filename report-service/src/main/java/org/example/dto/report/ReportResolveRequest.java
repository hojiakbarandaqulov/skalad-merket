package org.example.dto.report;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.enums.ReasonCode;
import org.example.enums.TargetType;

@Data
public class ReportResolveRequest {

    @NotNull(message = "resolutionNote required")
    private String resolutionNote;

}
