package org.example.client.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryValidationResponse {
    private Long categoryId;
    private boolean exists;
    private boolean active;
}
