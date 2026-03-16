package org.example.dto.kafka;

import lombok.*;
import org.example.enums.Roles;

import javax.management.relation.Role;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleUpdateEvent {
    private Long userId;
}
