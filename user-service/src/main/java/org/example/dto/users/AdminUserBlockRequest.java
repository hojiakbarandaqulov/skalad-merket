package org.example.dto.users;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminUserBlockRequest {
    @NotNull(message = "reason required")
    private String reason;
}
