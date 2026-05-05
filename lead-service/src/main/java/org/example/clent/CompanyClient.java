package org.example.clent;

import lombok.RequiredArgsConstructor;
import org.example.dto.internal.CompanyInternalSummaryResponse;
import org.example.exp.AppBadException;
import org.springframework.beans.factory.annotation.Value;
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
public class CompanyClient {

    private final RestTemplate restTemplate;

    @Value("${services.company-service.url}")
    private String companyServiceUrl;

    public CompanyInternalSummaryResponse getSummary(Long companyId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(getToken());

        HttpEntity<?> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<CompanyInternalSummaryResponse> response = restTemplate.exchange(
                    companyServiceUrl + "/internal/companies/" + companyId + "/summary",
                    HttpMethod.GET,
                    entity,
                    CompanyInternalSummaryResponse.class
            );
            return Objects.requireNonNull(response.getBody());
        } catch (HttpClientErrorException.NotFound e) {
            throw new AppBadException("Company not found: " + companyId);
        }
    }

    private String getToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            return jwtAuth.getToken().getTokenValue();
        }
        throw new AppBadException("No authentication token found");
    }
}
