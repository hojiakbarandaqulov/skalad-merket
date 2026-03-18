package org.example.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.enums.Currency;
import org.example.enums.PriceType;
import org.example.enums.ProductStatus;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
public class ProductResponse {
    private Long id;
    private Long sellerId;
    private Long companyId;
    private Long categoryId;
    private String name;
    private String shortDescription;
    private String description;
    private PriceType priceType;
    private BigDecimal price;
    private Currency currency;
    private Long regionId;
    private Long districtId;
    private ProductStatus status;
    private Map<String, String> attributes;
}
