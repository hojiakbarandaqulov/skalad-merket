package org.example.clent;

import lombok.RequiredArgsConstructor;
import org.example.dto.ApiResponse;
import org.example.dto.ProductResponse;
import org.example.exp.AppBadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class ProductClient {

    private final RestTemplate restTemplate;

    @Value("${services.product-service.url}")
    private String productServiceUrl;

    public ProductResponse getById(Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(getToken());

        HttpEntity<?> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<ApiResponse<ProductResponse>> response = restTemplate.exchange(
                    productServiceUrl + "/api/v1/products/" + id,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<>() {}
            );
            return Objects.requireNonNull(response.getBody()).getData();
        } catch (HttpClientErrorException.NotFound e) {
            throw new AppBadException("Product not found: " + id);
        }
    }

    private String getToken() {
        // SecurityContext dan tokenni olish
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            return jwtAuth.getToken().getTokenValue();
        }
        throw new AppBadException("No authentication token found");
    }
}