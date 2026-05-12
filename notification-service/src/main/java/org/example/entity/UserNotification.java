package org.example.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.example.entity.base.BaseEntity;

import java.util.List;

@Getter
@Setter
@Entity
public class UserNotification extends BaseEntity {

    private Long userId;

    private List<Notification> notification;
}
