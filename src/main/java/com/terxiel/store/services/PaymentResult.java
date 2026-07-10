package com.terxiel.store.services;

import com.terxiel.store.entities.OrderStatus;

public record PaymentResult(
        Long orderId,
        OrderStatus paymentStatus
) {
}
