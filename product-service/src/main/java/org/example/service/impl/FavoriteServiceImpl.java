package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dto.PagedResponse;
import org.example.dto.favorite.FavoriteResponse;
import org.example.dto.product.ProductResponse;
import org.example.entity.Favorite;
import org.example.enums.AppLanguage;
import org.example.exp.AppBadException;
import org.example.repository.FavoriteRepository;
import org.example.repository.ProductRepository;
import org.example.service.FavoriteService;
import org.example.service.ResourceBundleService;
import org.example.utils.SpringSecurityUtil;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final ProductRepository productRepository;
    private final ProductServiceImpl productService;
    private final ResourceBundleService messageService;

    @Override
    public PagedResponse<ProductResponse> getFavorites(int page, int perPage, AppLanguage language) {
        Long userId = requireProfileId(language);
        List<ProductResponse> items = favoriteRepository
                .findByUserIdAndDeletedFalse(userId, PageRequest.of(0, Integer.MAX_VALUE))
                .map(favorite -> productRepository
                        .findByIdAndIsActiveTrue(favorite.getProductId())
                        .map(productService::toResponse)
                        .orElse(null)
                )
                .filter(Objects::nonNull)
                .stream().collect(Collectors.toList());
        return ServiceHelper.toPagedResponse(items, page, perPage);
    }

    @Override
    public FavoriteResponse add(Long productId, AppLanguage language) {
        Long userId = requireProfileId(language);
        productRepository.findByIdAndIsActiveTrue(productId)
                .orElseThrow(() -> new AppBadException(messageService.getMessage("product.not.found", language)));
        if (favoriteRepository.findByUserIdAndProductIdAndDeletedFalse(userId, productId).isPresent()) {
            throw new AppBadException(messageService.getMessage("favorite.exists", language));
        }
        Favorite favorite = new Favorite();
        favorite.setUserId(userId);
        favorite.setProductId(productId);
        favoriteRepository.save(favorite);
        return new FavoriteResponse(true);
    }

    @Override
    public FavoriteResponse remove(Long productId, AppLanguage language) {
        Long userId = requireProfileId(language);
        Favorite favorite = favoriteRepository.findByUserIdAndProductIdAndDeletedFalse(userId, productId)
                .orElseThrow(() -> new AppBadException(messageService.getMessage("favorite.not.found", language)));
        favorite.setDeleted(Boolean.TRUE);
        favoriteRepository.save(favorite);
        return new FavoriteResponse(false);
    }

    private Long requireProfileId(AppLanguage language) {
        Long profileId = SpringSecurityUtil.getProfileId();
        if (profileId == null) {
            throw new AppBadException(messageService.getMessage("user.not.found", language));
        }
        return profileId;
    }
}
