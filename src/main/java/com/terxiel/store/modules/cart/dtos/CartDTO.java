package com.terxiel.store.modules.cart.dtos;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record CartDTO(
        UUID id,
        List<CartItemDTO> items,
        BigDecimal totalPrice
) {
    public CartDTO {
        items = (items == null) ? new ArrayList<>() : new ArrayList<>(items);
        totalPrice = (totalPrice == null) ? BigDecimal.ZERO : totalPrice;
    }
}