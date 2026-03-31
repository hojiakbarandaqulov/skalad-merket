package org.example.dto.kafka;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SuperAdminSendKeycloakId {
    private String keycloakId;
    private Long userId;
}
