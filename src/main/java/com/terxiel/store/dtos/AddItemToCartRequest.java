package com.terxiel.store.dtos;

import jakarta.validation.constraints.NotNull;

public record AddItemToCartRequest(
        @NotNull Long productId
) {
}
