package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.ApiResponse;
import org.example.dto.PagedResponse;
import org.example.dto.favorite.FavoriteResponse;
import org.example.dto.product.ProductResponse;
import org.example.enums.AppLanguage;
import org.example.service.FavoriteService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/favorites")
public class FavoriteController {
    private final FavoriteService favoriteService;

    @GetMapping
    public ApiResponse<PagedResponse<ProductResponse>> getFavorites(@RequestParam(defaultValue = "1") int page,
                                                                    @RequestParam(defaultValue = "20") int perPage,
                                                                    @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language) {
        return ApiResponse.successResponse(favoriteService.getFavorites(page, perPage, language));
    }

    @PostMapping("/{productId}")
    public ApiResponse<FavoriteResponse> add(@PathVariable Long productId,
                                             @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language) {
        return ApiResponse.successResponse(favoriteService.add(productId, language));
    }

    @DeleteMapping("/{productId}")
    public ApiResponse<FavoriteResponse> remove(@PathVariable Long productId,
                                                @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language) {
        return ApiResponse.successResponse(favoriteService.remove(productId, language));
    }
}
