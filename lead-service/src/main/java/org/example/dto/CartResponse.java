package org.example.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CartResponse {
    private Long itemCount;
    private Long totalQuantity;
    private List<CartItemResponse> items;
}
