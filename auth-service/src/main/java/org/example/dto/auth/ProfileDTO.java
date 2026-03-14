package org.example.dto.auth;

import lombok.Data;
import org.example.enums.Roles;

@Data
public class ProfileDTO {
    private String firstName;
    private String lastName;
    private String username;
    private Roles role;
    private String accessToken;
    private String refreshToken;
    private Integer expiresIn;
}
