package org.example.dto.kafka;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserVerifiedEvent {
    private Long userId;
}
