package org.example.dto.users;

import lombok.Getter;
import lombok.Setter;
import org.example.enums.GeneralStatus;
import org.example.enums.Roles;

import java.time.LocalDateTime;

@Getter
@Setter
public class AdminUserDetailResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String fullName;
    private String username;
    private String position;
    private String telegram;
    private String extraPhone;
    private GeneralStatus status;
    private Roles roles;
    private Integer warningCount;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
}
