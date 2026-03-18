package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.example.enums.DataType;

@Getter
@Setter
public class CategoryAttributeCreateRequest {

    @NotBlank
    private String code;

    @NotBlank
    private String label;

    @NotNull
    private DataType dataType;

    private Boolean isRequired = Boolean.FALSE;
    private Boolean isFilterable = Boolean.FALSE;
    private String optionsJson;
    private Integer sortOrder;
}