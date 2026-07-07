package com.terxiel.store.controllers;

import com.terxiel.store.dtos.OrderDTO;
import com.terxiel.store.services.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("orders")
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public List<OrderDTO.Order> getOrders()
    {
        return orderService.getOrders();
    }
}
