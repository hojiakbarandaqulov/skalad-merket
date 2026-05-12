package org.example.dto;

import lombok.Builder;
import lombok.Getter;
import org.example.enums.Currency;

import java.math.BigDecimal;

@Getter
@Builder
public class CartItemResponse {
    private Long id;
    private Long productId;
    private Long sellerId;
    private Long companyId;
    private String productName;
    private String productSlug;
    private String primaryImage;
    private BigDecimal price;
    private Currency currency;
    private String companyName;
    private String companySlug;
    private String companyLogoPath;
    private Integer quantity;
}
