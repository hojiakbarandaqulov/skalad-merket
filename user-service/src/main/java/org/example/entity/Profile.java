package org.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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


    @Enumerated(EnumType.STRING)
    private Roles role;

    @Enumerated(EnumType.STRING)
    private GeneralStatus status;


   /* @OneToOne(mappedBy = "profile", cascade = CascadeType.ALL)
    private Attach avatar;*/
}
