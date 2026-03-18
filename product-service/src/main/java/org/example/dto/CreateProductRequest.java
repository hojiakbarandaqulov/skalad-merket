package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.example.enums.Currency;
import org.example.enums.PriceType;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
public class CreateProductRequest {

    @NotNull
    private Long companyId;

    @NotNull
    private Long categoryId;

    @NotBlank
    private String name;

    private String shortDescription;
    private String description;

    @NotNull
    private PriceType priceType;

    private BigDecimal price;

    @NotNull
    private Currency currency;

    @NotNull
    private Long regionId;

    private Long districtId;

    private Map<String, String> attributes;
}
