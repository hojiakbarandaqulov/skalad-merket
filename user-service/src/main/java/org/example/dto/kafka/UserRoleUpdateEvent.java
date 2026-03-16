package org.example.dto.kafka;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleUpdateEvent {
    private Long userId;
}
