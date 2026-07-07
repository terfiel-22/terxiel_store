package com.terxiel.store.services;

import com.terxiel.store.dtos.OrderDTO;
import com.terxiel.store.exceptions.OrderNotFoundException;
import com.terxiel.store.mappers.OrderMapper;
import com.terxiel.store.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class OrderService {
    private AuthService authService;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public List<OrderDTO.Order> getOrders()
    {
        var user = authService.getCurrentUser();
        var orders = orderRepository.getOrdersByCustomer(user);
        return orders.stream().map(orderMapper::toDto).toList();
    }

    public OrderDTO.Order getOrder(Long id)
    {
        // Get the order by the id. If not found, throw an error
        var order = orderRepository.getOrderWithItems(id).orElseThrow(OrderNotFoundException::new);

        // Check if the request is not from the owner.
        var user = authService.getCurrentUser();
        if(!order.isPlacedBy(user))
        {
            throw new AccessDeniedException("You don't have access to this order.");
        }

        return orderMapper.toDto(order);
    }
}
