package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenDTO {
    @NotBlank(message = "refreshToken required")
    private String refreshToken;
}
