package com.terxiel.store.controllers;

import com.terxiel.store.dtos.ErrorDTO;
import com.terxiel.store.dtos.OrderDTO;
import com.terxiel.store.exceptions.OrderNotFoundException;
import com.terxiel.store.services.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}")
    public OrderDTO.Order getOrder(@PathVariable Long id)
    {
        return orderService.getOrder(id);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleOrderNotFound()
    {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO("Order not found."));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDTO> handleAccessDenied(AccessDeniedException ex)
    {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorDTO(ex.getMessage()));
    }
}
