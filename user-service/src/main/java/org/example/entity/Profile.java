package org.example.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.entity.base.BaseEntity;
import org.example.enums.GeneralStatus;
import org.example.enums.Roles;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Profile extends BaseEntity {

    @Column(unique = true, nullable = false)
    private Long userId;

    private String username;
    private String fullName;
    private String position;
    private String telegram;
    @Column(length = 13)
    private String extraPhone;
    private String password;

    @Enumerated(EnumType.STRING)
    private GeneralStatus status;

    @Enumerated(EnumType.STRING)
    private Roles roles;
    @ManyToOne
    private Attach photo;
}
