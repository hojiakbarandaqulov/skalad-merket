package org.example.dto.users;

import lombok.Getter;
import lombok.Setter;
import org.example.enums.GeneralStatus;
import org.example.enums.Roles;

import java.time.LocalDateTime;

@Getter
@Setter
public class UsersResponse {
    private Long id;
    private String name;
    private String username;
    private GeneralStatus status;
    private Integer warningCount;
    private Roles roles;
    private LocalDateTime createdDate;
}
