package org.example.service;

import org.example.dto.CompanyResponseDTO;
import org.example.dto.favorite.FavoriteCountResponse;
import org.example.enums.AppLanguage;
import org.springframework.data.domain.PageImpl;

public interface FavoriteService {
    Boolean createFavorite(Long companyId, AppLanguage language);

    FavoriteCountResponse getCount(AppLanguage language);

    Boolean remove(Long companyId, AppLanguage language);

    PageImpl<CompanyResponseDTO> getFavorites(int page, int perPage, AppLanguage language);
}
