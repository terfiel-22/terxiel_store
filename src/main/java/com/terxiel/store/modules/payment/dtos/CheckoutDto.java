package com.terxiel.store.modules.payment.dtos;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public interface CheckoutDto
{
        record Request(
                @NotNull(message = "Cart ID is required.")
                UUID cartId
        ){}

        record Response(
                Long orderId,
                String checkoutUrl
        ){}
}
