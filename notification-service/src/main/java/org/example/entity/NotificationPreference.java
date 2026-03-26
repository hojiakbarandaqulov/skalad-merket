package org.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.example.entity.base.BaseEntity;

@Getter
@Setter
@Entity
@Table(name = "notification_preferences")
public class NotificationPreference extends BaseEntity {
    @Column(nullable = false, unique = true)
    private Long userId;

    @Column(nullable = false)
    private Boolean inAppEnabled = Boolean.TRUE;

    @Column(nullable = false)
    private Boolean pushEnabled = Boolean.TRUE;

    @Column(nullable = false)
    private Boolean emailEnabled = Boolean.FALSE;
}
