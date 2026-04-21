package org.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;
import org.example.entity.base.BaseEntity;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Setter
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"userId", "productId"}))
public class Favorite extends BaseEntity {
    private Long userId;
    private Long productId;

}
