package org.example.dto.admin;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PromotionRequest {
    private String promotionType;
    private LocalDateTime startsAt;
    private LocalDateTime endsAt;
}
