package org.example.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.enums.Roles;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRole {
    private Long userId;
    private Roles roles;
}
