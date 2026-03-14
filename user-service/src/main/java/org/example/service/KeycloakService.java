package org.example.service;

import org.example.dto.TokenResponseDTO;

public interface KeycloakService {
    String createUser(String firstName, String lastName, String username, String password);

    void addProfileIdAttribute(String keycloakId, Long profileId,
                              String firstName, String lastName, String email);


}
