package org.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.entity.base.BaseEntity;
import org.example.entity.base.BaseTime;

@Getter
@Setter
@Entity
public class ProductImage extends BaseTime {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private String storageKey;

    private Integer sortOrder;

    private Boolean isPrimary = Boolean.FALSE;

    private String mimeType;

    private Long fileSize;

    private Integer width;

    private Integer height;
}
