package org.example.service;

import org.example.dto.TokenResponseDTO;

public interface KeycloakService {
    TokenResponseDTO getToken(String username, String password);
    TokenResponseDTO refreshToken(String refreshToken);

    String createUser(String firstName, String lastName, String username, String password);

    void deleteUser(String username);

    void addProfileIdAttribute(String keycloakId, Long profileId,
                              String firstName, String lastName, String email, String password);

//    void verifyUserEmail(String username);

}
