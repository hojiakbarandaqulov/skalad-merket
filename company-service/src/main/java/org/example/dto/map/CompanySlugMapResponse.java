package org.example.dto.map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanySlugMapResponse {
    private Long id;
    private String name;
    private String slug;
    private Long regionId;
    private Long districtId;
    private String address;
    private String lat;
    private String lng;
}
