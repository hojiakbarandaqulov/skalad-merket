package org.example.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CartCheckoutResponse {
    private Integer createdCount;
    private List<LeadResponse> leads;
}
