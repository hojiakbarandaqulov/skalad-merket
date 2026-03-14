
package org.example.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;
import org.example.entity.base.BaseEntity;
import org.example.enums.EmailType;

@Getter
@Setter
@Entity
public class EmailHistory extends BaseEntity {
    private String code;

    private String email;

    @Enumerated(EnumType.STRING)
    private EmailType emailType;

    private Integer attemptCount;

}
