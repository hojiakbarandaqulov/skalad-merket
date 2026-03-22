package org.example.service;

import org.example.dto.PagedResponse;
import org.example.dto.favorite.FavoriteResponse;
import org.example.dto.product.ProductResponse;
import org.example.enums.AppLanguage;

public interface FavoriteService {
    PagedResponse<ProductResponse> getFavorites(int page, int perPage, AppLanguage language);

    FavoriteResponse add(Long productId, AppLanguage language);

    FavoriteResponse remove(Long productId, AppLanguage language);
}
