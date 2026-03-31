package org.example.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.enums.GeneralStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateStatus {
    private Long userId;
    private GeneralStatus status;
}
