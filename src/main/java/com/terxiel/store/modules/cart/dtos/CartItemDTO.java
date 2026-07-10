package com.terxiel.store.modules.cart.dtos;

import java.math.BigDecimal;

public record CartItemDTO(
        CartProductDTO product,
        Integer quantity,
        BigDecimal totalPrice
) {
}
