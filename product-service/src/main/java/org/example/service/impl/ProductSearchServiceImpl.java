package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.document.ProductDocument;
import org.example.dto.PagedResponse;
import org.example.dto.product.*;
import org.example.enums.AppLanguage;
import org.example.enums.ProductModerationStatus;
import org.example.repository.ProductSearchRepository;
import org.example.service.ProductSearchService;
import org.example.service.ProductService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductSearchServiceImpl implements ProductSearchService {
    private final ProductSearchRepository repository;
    private final ProductSearchRepository searchRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    public void index(ProductDocument document) {
        repository.save(document);
    }

    @Override
    public void delete(Long productId) {
        repository.deleteById(productId.toString());
    }

    @Override
    public void update(ProductDocument document) {
        Optional<ProductDocument> byId = repository.findById(document.getId());
        if (byId.isPresent()) {
            repository.save(document);
        }
    }

    @Override
    public List<ProductSearchResponse> search(String query, int page, int perPage) {
        NativeQuery searchQuery = NativeQuery.builder()
                .withQuery(q -> q
                        .bool(b -> b
                                .should(s -> s
                                        .match(m -> m
                                                .field("name")
                                                .query(query)
                                                .fuzziness("AUTO")
                                                .boost(3.0f)
                                        )
                                )
                                .should(s -> s
                                        .match(m -> m
                                                .field("shortDescription")
                                                .query(query)
                                                .fuzziness("AUTO")
                                                .boost(1.0f)
                                        )
                                ).filter(f -> f
                                        .term(t -> t
                                                .field("moderationStatus")
                                                .value("APPROVED")
                                        )
                                ).minimumShouldMatch("1")
                        )
                )
                .withPageable(PageRequest.of(page - 1, perPage)).build();
        SearchHits<ProductDocument> hits = elasticsearchOperations.search(searchQuery, ProductDocument.class);


        return hits.stream()
                .map(SearchHit::getContent)
                .map(this::toSearchResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PagedResponse<ProductResponse> productSearch(String q, String categoryId, Long regionId, int page, int perPage, AppLanguage language) {
        NativeQuery searchQuery = NativeQuery.builder()
                .withQuery(query -> query
                        .bool(b -> b
                                .should(s -> s
                                        .match(m -> m
                                                .field("name")
                                                .query(1)
                                                .fuzziness("AUTO")
                                                .boost(3.0f)
                                        )
                                )
                                .should(s -> s
                                        .match(m -> m
                                                .field("shortDescription")
                                                .query(1)
                                                .fuzziness("AUTO")
                                                .boost(1.0f)
                                        )
                                ).filter(f -> f
                                        .term(t -> t
                                                .field("moderationStatus")
                                                .value("APPROVED")
                                        )
                                )
                                .filter(f -> f
                                        .term(t -> t
                                                .field("regionId")
                                                .value(regionId))
                                )
                                .filter(f -> f
                                        .term(t -> t
                                                .field("categoryId")
                                                .value(categoryId))
                                )
                                .minimumShouldMatch("1")

                        )
                )
                .withPageable(PageRequest.of(page - 1, perPage)).build();
        SearchHits<ProductDocument> hits = elasticsearchOperations.search(searchQuery, ProductDocument.class);


        return new PagedResponse<>(hits.stream()
                .map(SearchHit::getContent)
                .map(this::toSearchResponse));
    }

    private ProductSearchResponse toSearchResponse(ProductDocument doc) {
        return ProductSearchResponse.builder()
                .id(Long.parseLong(doc.getId()))
                .name(doc.getName())
                .slug(doc.getSlug())
                .price(doc.getPrice())
                .currency(doc.getCurrency())
                .primaryImageUrl(doc.getPrimaryImageUrl())
                .build();
    }
}
