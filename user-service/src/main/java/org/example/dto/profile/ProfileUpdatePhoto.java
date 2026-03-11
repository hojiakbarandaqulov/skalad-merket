package org.example.dto.profile;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProfileUpdatePhoto {
    @NotBlank(message = "photoId required")
    private String photoId;
}
