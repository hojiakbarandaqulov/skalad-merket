package org.example.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDTO {
    @NotBlank(message = "username required")
    private String username;
    @NotBlank(message = "password required")
    private String password;
}
