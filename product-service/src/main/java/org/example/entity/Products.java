package org.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;
import org.example.entity.base.BaseEntity;
import org.example.enums.ModerationStatus;
import org.example.enums.PriceType;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Getter
@Setter
public class Products extends BaseEntity {
    @Column(nullable = false)
    private Long companyId;

    @Column(nullable = false)
    private Long categoryId;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String slug;

    private String shortDescription;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PriceType priceType;

    private BigDecimal price;

    private String currency;

    private String unitLabel;

    private BigDecimal quantity;

    private Long regionId;

    private Long districtId;

    @Column(columnDefinition = "jsonb")
    private String attributesJsonb;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ModerationStatus moderationStatus = ModerationStatus.PENDING;

    private Boolean isActive = Boolean.TRUE;
    private Boolean isPromoted = Boolean.FALSE;
    private Instant promotedUntil;
    private String rejectReason;
    private Long viewsCountCache = 0L;

    private Long favoritesCountCache = 0L;

    private Instant deletedAt;

}