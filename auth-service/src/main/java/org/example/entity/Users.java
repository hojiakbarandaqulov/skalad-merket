package org.example.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;
import org.example.entity.base.BaseEntity;
import org.example.enums.GeneralStatus;
import org.example.enums.Roles;

@Getter
@Setter
@Entity
public class Users extends BaseEntity {
    private String fullName;

    private String username;

    private String password;

    @Enumerated(EnumType.STRING)
    private Roles role;

    @Enumerated(EnumType.STRING)
    private GeneralStatus status;

}
