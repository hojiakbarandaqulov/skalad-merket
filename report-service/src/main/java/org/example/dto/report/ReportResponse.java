package org.example.dto.report;

import lombok.Getter;
import lombok.Setter;
import org.example.enums.ReasonCode;
import org.example.enums.ReportStatus;
import org.example.enums.TargetType;

import java.time.LocalDateTime;

@Setter
@Getter
public class ReportResponse {
    private Long id;
    private ReportStatus status;
    private TargetType targetType;
    private Long targetId;
    private ReasonCode reasonCode;
    private LocalDateTime createdDate;
}
