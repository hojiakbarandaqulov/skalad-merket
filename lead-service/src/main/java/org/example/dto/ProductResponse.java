package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.example.enums.Currency;
import org.example.enums.PriceType;
import org.example.enums.ProductModerationStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Setter
@Getter
public class ProductResponse {
    private Long id;

    @JsonProperty("company_id")
    private Long companyId;

    @JsonProperty("seller_id")
    private Long sellerId;

    private Long categoryId;

    private String name;
    private String slug;

    @JsonProperty("short_description")
    private String shortDescription;

    private String description;

    @JsonProperty("price_type")
    private PriceType priceType;

    private BigDecimal price;
    private Currency currency;

    @JsonProperty("region_id")
    private Long regionId;

    @JsonProperty("district_id")
    private Long districtId;

    private Map<String, Object> attributes;
    private ProductModerationStatus status;

    @JsonProperty("is_active")
    private Boolean isActive;

    @JsonProperty("is_promoted")
    private Boolean isPromoted;

    @JsonProperty("promoted_until")
    private LocalDateTime promotedUntil;

    @JsonProperty("reject_reason")
    private String rejectReason;

    @JsonProperty("views_count_cache")
    private Long viewsCountCache;

    @JsonProperty("favorites_count_cache")
    private Long favoritesCountCache;

    @JsonProperty("created_by")
    private Long createdBy;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    private List<ProductImageResponse> images;
}
