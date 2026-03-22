package org.example.service;

import org.example.dto.*;
import org.example.dto.product.ProductResponse;
import org.example.enums.AppLanguage;

public interface CatalogService {
    PagedResponse<ProductResponse> getCatalog(String q, String category, Long regionId, String currency, int page, int perPage, AppLanguage language);

    PagedResponse<ProductResponse> search(String q, String category, Long regionId, int page, int perPage, AppLanguage language);

    SuggestionResponse suggestions(String q, AppLanguage language);

    CatalogFilterResponse filters(String category, AppLanguage language);

    CatalogHomepageResponse homepage(AppLanguage language);
}
