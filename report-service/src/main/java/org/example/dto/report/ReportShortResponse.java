package org.example.dto.report;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.enums.ReportStatus;

@Getter
@Setter
@AllArgsConstructor
@NotNull
public class ReportShortResponse {
    private Long reportId;
    private ReportStatus reportStatus;
}
