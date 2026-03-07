package org.example.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdatePasswordDTO {
    @NotBlank(message = "email required")
    private String username;

    @NotBlank(message = "confirm Code required")
    private String confirmCode;

    @NotBlank(message = "newPassword required")
    private String newPassword;

}
