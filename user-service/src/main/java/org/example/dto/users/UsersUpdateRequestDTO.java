package org.example.dto.users;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsersUpdateRequestDTO {
    @NotBlank(message = "fullName required")
    private String firstName;

    @NotBlank(message = "fullName required")
    private String lastName;

    @NotBlank(message = "position required")
    private String position;
    @NotBlank(message = "telegram required")
    private String telegram;
    @NotBlank(message = "extraPhone required")
    private String extraPhone;
}
