package org.example.dto.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class ChatProductSummaryResponse {
    private Long id;
    private String name;
    private String slug;
    private BigDecimal price;
    private String currency;

    @JsonProperty("primary_image")
    private String primaryImage;
}
