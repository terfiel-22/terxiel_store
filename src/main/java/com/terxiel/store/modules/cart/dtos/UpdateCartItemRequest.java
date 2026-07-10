package com.terxiel.store.modules.cart.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for {@link com.terxiel.store.entities.CartItem}
 */
public record UpdateCartItemRequest (
        @NotNull(message = "Quantity is required.")
        @Min(value = 1, message = "Quantity must be not less than 1.")
        @Max(value = 10, message = "Quantity must be not greater than 100.")
        Integer quantity
) {
}