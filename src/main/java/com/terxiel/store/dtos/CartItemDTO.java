package com.terxiel.store.dtos;

import java.math.BigDecimal;

public record CartItemDTO(
        CartProductDTO product,
        Integer quantity,
        BigDecimal totalPrice
) {
}
