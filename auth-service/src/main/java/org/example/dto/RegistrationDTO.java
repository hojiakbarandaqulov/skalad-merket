package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.example.enums.Roles;

@Getter
@Setter
public class RegistrationDTO {
    @NotBlank(message = "firstName required")
    private String firstName;

    @NotBlank(message = "lastName required")
    private String lastName;

    @NotBlank(message = "username required")
    private String username;

    @NotBlank(message = "password required")
    private String password;
}
