package org.example.dto.banner;

import lombok.Data;
import org.example.enums.PlacementCode;

import java.time.LocalDateTime;

@Data
public class BannerCreateResponse {
    private Long id;
    private String targetUrl;
    private PlacementCode placementCode;
    private LocalDateTime startsAt;
    private LocalDateTime endsAt;
    private Boolean isActive;
}
