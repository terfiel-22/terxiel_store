package com.terxiel.store.dtos;

import com.terxiel.store.entities.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderDTO {
    record Order(
            Long id,
            OrderStatus status,
            LocalDateTime createdAt,
            List<OrderItem> orderItems,
            BigDecimal totalPrice
    ){}

    record OrderItem(
            OrderProduct product,
            Integer quantity,
            BigDecimal totalPrice
    ){}

    record OrderProduct(
            Long id,
            String name,
            BigDecimal price
    ){}
}
