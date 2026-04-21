package org.example.dto.banner;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.enums.PlacementCode;

import java.time.LocalDateTime;

@Data
public class BannerCreate {
    @NotNull(message = "placementCode required")
    private PlacementCode placementCode;
    @NotBlank(message = "targetUrl required")
    private String targetUrl;
    @NotNull(message = "startsAt required")
    private LocalDateTime startsAt;
    @NotNull(message = "endsAt required")
    private LocalDateTime endsAt;
}
