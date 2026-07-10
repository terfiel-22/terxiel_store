package com.terxiel.store.modules.payment.dtos;

import com.terxiel.store.entities.OrderStatus;

public record PaymentResult(
        Long orderId,
        OrderStatus paymentStatus
) {
}
