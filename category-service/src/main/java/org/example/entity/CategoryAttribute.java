package org.example.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.entity.base.BaseEntity;
import org.example.enums.DataType;

@Entity
@Getter
@Setter
@Table(
        name = "category_attribute",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"category_id", "code"})
        }
)
public class CategoryAttribute extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String label;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DataType dataType;

    private Boolean isRequired = Boolean.FALSE;
    private Boolean isFilterable = Boolean.FALSE;

    @Column(columnDefinition = "TEXT")
    private String optionsJson;

    private Integer sortOrder;
}
