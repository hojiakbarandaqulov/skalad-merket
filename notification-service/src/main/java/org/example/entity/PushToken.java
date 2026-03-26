package org.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.example.entity.base.BaseEntity;
import org.example.enums.PushPlatform;

@Getter
@Setter
@Entity
@Table(name = "push_tokens")
public class PushToken extends BaseEntity {
    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, unique = true, length = 1024)
    private String token;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PushPlatform platform;
}
