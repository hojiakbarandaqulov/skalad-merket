package org.example.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.TokenResponseDTO;
import org.example.exp.AppBadException;
import org.example.service.KeycloakService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeycloakServiceImpl implements KeycloakService {
    @Value("${keycloak.token-url}")
    private String tokenUrl;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    @Value("${keycloak.admin-url}")
    private String adminUrl;

    @Value("${keycloak.admin-username}")
    private String adminUsername;

    @Value("${keycloak.admin-password}")
    private String adminPassword;

    private final RestTemplate restTemplate;


    @Override
    public String createUser(String firstName, String lastName, String username, String password) {
        try {
            String adminToken = getAdminToken();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(adminToken);

            log.info("Creating user: firstName={}, lastName={}, username={}", firstName, lastName, username);

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> user = new LinkedHashMap<>();
            user.put("username", username);
            user.put("email", username);
            user.put("firstName", firstName);
            user.put("lastName", lastName);
            user.put("enabled", true);
            user.put("emailVerified", true);
            user.put("requiredActions", Collections.emptyList());

            Map<String, Object> credential = new HashMap<>();
            credential.put("type", "password");
            credential.put("value", password);
            credential.put("temporary", false);

            user.put("credentials", List.of(credential));

            log.info("Keycloak ga yuborilayotgan JSON: {}", mapper.writeValueAsString(user));

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(user, headers);
            ResponseEntity<Void> response = restTemplate.postForEntity(adminUrl + "/users", request, Void.class);

            String location = response.getHeaders().getFirst("Location");
            assert location != null;
            String keycloakId = location.substring(location.lastIndexOf("/") + 1);

            log.info("Keycloak da user yaratildi: {}, keycloakId: {}", username, keycloakId);
            return keycloakId; // ← qaytarish

        } catch (Exception e) {
            log.error("Keycloak da user yaratishda xato: {}", e.getMessage(), e);
            throw new AppBadException("Keycloak da user yaratishda xato: " + e.getMessage());
        }
    }

    @Override
    public void addProfileIdAttribute(String keycloakId, Long profileId,
                                      String firstName, String lastName, String email) {
        String adminToken = getAdminToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(adminToken);

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("profileId", List.of(String.valueOf(profileId)));

        Map<String, Object> userUpdate = new HashMap<>();
        userUpdate.put("firstName", firstName);   // ← qo'shing
        userUpdate.put("lastName", lastName);     // ← qo'shing
        userUpdate.put("email", email);           // ← qo'shing
        userUpdate.put("emailVerified", true);    // ← qo'shing
        userUpdate.put("attributes", attributes);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(userUpdate, headers);
        restTemplate.exchange(
                adminUrl + "/users/" + keycloakId,
                HttpMethod.PUT,
                request,
                Void.class
        );

        log.info("ProfileId attribute qo'shildi: keycloakId={}, profileId={}", keycloakId, profileId);
    }

    private String getAdminToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("client_id", "admin-cli");
        body.add("username", adminUsername);
        body.add("password", adminPassword);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        try {
            log.info("Token request body: {}", body);  // DEBUG
            ResponseEntity<TokenResponseDTO> response = restTemplate.postForEntity(
                    "http://localhost:9090/realms/master/protocol/openid-connect/token",
                    request, TokenResponseDTO.class);  // TokenResponse DTO yarating

            if (response.getStatusCode().is2xxSuccessful()) {
                String token = response.getBody().getAccessToken();
                log.info("Admin token muvaffaqiyatli olindi");
                return token;
            } else {
                log.error("Token response status: {}, body: {}",  response.getStatusCode(), Optional.ofNullable(response.getBody()));
            }
        } catch (HttpClientErrorException e) {
            log.error("Token error: status={}, body={}", e.getStatusCode(), e.getResponseBodyAsString());
        }
        throw new RuntimeException("Admin token olinmadi");
    }
}
