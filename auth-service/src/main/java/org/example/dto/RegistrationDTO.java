package org.example.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationDTO {
    @NotBlank(message = "fullName required")
    private String fullName;

    @NotBlank(message = "username required")
    private String username;

    @NotBlank(message = "password required")
    private String password;
}
