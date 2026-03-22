package org.example.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.example.entity.base.BaseEntity;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class LeadItem extends BaseEntity {
    private Long leadId;
    private Long productId;
    private String productNameSnapshot;
    private BigDecimal priceSnapshot;
    private Integer quantity;
}
