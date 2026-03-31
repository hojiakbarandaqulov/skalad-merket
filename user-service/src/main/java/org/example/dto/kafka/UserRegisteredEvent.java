package org.example.dto.kafka;

import lombok.*;
import org.example.enums.GeneralStatus;
import org.example.enums.Roles;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegisteredEvent {
    private Long userId;
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private Roles roles;
    private GeneralStatus status;
    private String keycloakId;
}