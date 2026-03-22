package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.*;
import org.example.dto.product.ProductResponse;
import org.example.enums.AppLanguage;
import org.example.service.CatalogService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/catalog")
public class CatalogController {
    
    private final CatalogService catalogService;

    @GetMapping
    public ApiResponse<PagedResponse<ProductResponse>> getCatalog(@RequestParam(required = false) String q,
                                                                  @RequestParam(required = false) String category,
                                                                  @RequestParam(required = false) Long regionId,
                                                                  @RequestParam(required = false) String currency,
                                                                  @RequestParam(defaultValue = "1") int page,
                                                                  @RequestParam(defaultValue = "20") int perPage,
                                                                  @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language) {
        return ApiResponse.successResponse(catalogService.getCatalog(q, category, regionId, currency, page, perPage, language));
    }

    @GetMapping("/search")
    public ApiResponse<PagedResponse<ProductResponse>> search(@RequestParam(required = false) String q,
                                                              @RequestParam(required = false) String category,
                                                              @RequestParam(required = false) Long regionId,
                                                              @RequestParam(defaultValue = "1") int page,
                                                              @RequestParam(defaultValue = "20") int perPage,
                                                              @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language) {
        return ApiResponse.successResponse(catalogService.search(q, category, regionId, page, perPage, language));
    }

    @GetMapping("/search/suggestions")
    public ApiResponse<SuggestionResponse> suggestions(@RequestParam String q,
                                                       @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language) {
        return ApiResponse.successResponse(catalogService.suggestions(q, language));
    }

    @GetMapping("/filters")
    public ApiResponse<CatalogFilterResponse> filters(@RequestParam(required = false) String category,
                                                      @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language) {
        return ApiResponse.successResponse(catalogService.filters(category, language));
    }

    @GetMapping("/homepage")
    public ApiResponse<CatalogHomepageResponse> homepage(@RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage language) {
        return ApiResponse.successResponse(catalogService.homepage(language));
    }
}
