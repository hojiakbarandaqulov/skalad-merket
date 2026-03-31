package org.example.dto.admin;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModerationDecisionRequest {
    private String reasonCode;
    private String comment;
}
