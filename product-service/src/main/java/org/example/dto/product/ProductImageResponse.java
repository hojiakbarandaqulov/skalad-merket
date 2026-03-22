package org.example.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class ProductImageResponse {
    private String id;
    private String url;

    @JsonProperty("thumbnail_urls")
    private Map<String, String> thumbnailUrls;

    @JsonProperty("is_primary")
    private Boolean isPrimary;
}
