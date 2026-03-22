package org.example.client.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategorySummaryResponse {
    private Long id;
    private String name;
    private String slug;
}
