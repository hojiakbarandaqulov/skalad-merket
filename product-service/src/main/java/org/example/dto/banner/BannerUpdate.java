package org.example.dto.banner;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BannerUpdate {
    @NotBlank(message = "targetUrl required")
    private String targetUrl;
    @NotNull(message = "endsAt required")
    private LocalDateTime endsAt;
    private Boolean isActive;
}
