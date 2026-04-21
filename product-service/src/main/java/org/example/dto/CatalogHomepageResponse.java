package org.example.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.dto.product.ProductResponse;

import java.util.List;

@Getter
@Setter
@Builder
public class CatalogHomepageResponse {
    private List<ProductResponse> featuredProducts;
    private List<ProductResponse> newProducts;
    private List<String> banners;
    private List<Long> topCategories;
    private List<Long> verifiedCompanies;
}
