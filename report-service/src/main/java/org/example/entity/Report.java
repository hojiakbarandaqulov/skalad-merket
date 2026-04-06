package org.example.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.enums.ReasonCode;
import org.example.enums.ReportStatus;
import org.example.enums.TargetType;
import org.example.entity.base.BaseEntity;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Report extends BaseEntity {

    private Long reporterUserId;

    @Enumerated(EnumType.STRING)
    private TargetType targetType;

    private Long targetId;

    @Enumerated(EnumType.STRING)
    private ReasonCode reasonCode;

    private String comment;

    @Enumerated(EnumType.STRING)
    private ReportStatus status=ReportStatus.NEW;

    private Long resolvedBy;
    private LocalDateTime resolvedAt;
    private String resolutionNote;
}
