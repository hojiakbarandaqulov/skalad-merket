package org.example.dto.users;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UsersUpdatePhoto {
    @NotBlank(message = "photoId required")
    private String photoId;
}
