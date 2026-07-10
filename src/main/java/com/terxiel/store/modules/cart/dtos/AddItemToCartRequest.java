package com.terxiel.store.modules.cart.dtos;

import jakarta.validation.constraints.NotNull;

public record AddItemToCartRequest(
        @NotNull Long productId
) {
}
