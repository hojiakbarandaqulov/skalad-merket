package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyRequestDTO {
    @NotBlank(message = "name required")
    private String name;

    private String shortDescription;
    private String description;

    @NotBlank(message = "stir required")
    private String stir;

    @NotBlank(message = "phonePrimary required")
    private String phonePrimary;

    private String phoneSecondary;
    private String website;

    @NotNull
    private Long regionId;

    @NotNull
    private Long districtId;

    @NotBlank
    private String address;
}