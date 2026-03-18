package org.example.dto.categoryAtribute;

import lombok.Getter;
import lombok.Setter;
import org.example.enums.DataType;

@Getter
@Setter
public class CategoryAttributeResponse {
    private Long id;
    private String code;
    private String label;
    private DataType dataType;
    private Boolean isRequired;
    private Boolean isFilterable;
    private String optionsJson;
    private Integer sortOrder;
}
