package org.example.dto.auth;

import lombok.Data;
import org.example.enums.Roles;

@Data
public class ProfileDTO {
    private String fullName;
    private String username;
    private Roles role;
    private String jwt;
}
