package org.example.dto.report;

import lombok.Getter;
import lombok.Setter;
import org.example.enums.ReasonCode;
import org.example.enums.ReportStatus;
import org.example.enums.TargetType;

import java.time.LocalDateTime;

@Setter
@Getter
public class ReportResolveResponse {
    private Long id;
    private ReportStatus status;
    private Long resolvedBy;
    private LocalDateTime resolvedAt;
    private String resolutionNote;
}
