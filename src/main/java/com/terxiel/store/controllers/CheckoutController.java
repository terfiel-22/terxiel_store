package com.terxiel.store.controllers;

import com.terxiel.store.dtos.CheckoutDto;
import com.terxiel.store.entities.Order;
import com.terxiel.store.entities.OrderItem;
import com.terxiel.store.entities.OrderStatus;
import com.terxiel.store.exceptions.CartIsEmptyException;
import com.terxiel.store.exceptions.CartNotFoundException;
import com.terxiel.store.repositories.CartRepository;
import com.terxiel.store.repositories.OrderRepository;
import com.terxiel.store.services.AuthService;
import com.terxiel.store.services.CartService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("checkout")
public class CheckoutController {

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final AuthService authService;
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<CheckoutDto.Response> checkout(
            @Valid @RequestBody CheckoutDto.Request request
    )
    {
        // Get cart from request
        var cart = cartRepository.getCartWithItems(request.cartId()).orElseThrow(CartNotFoundException::new);
        if(cart.getCartItems().isEmpty())
        {
            throw new CartIsEmptyException();
        }

        // Get current user
        var user = authService.getCurrentUser();


        var order = Order.builder()
                .totalPrice(cart.getTotalPrice())
                .status(OrderStatus.PENDING)
                .customer(user)
                .build();

        // Set order items
        cart.getCartItems().forEach(cartItem -> {
            var orderItem = OrderItem.builder()
                    .order(order)
                    .product(cartItem.getProduct())
                    .quantity(cartItem.getQuantity())
                    .unitPrice(cartItem.getProduct().getPrice())
                    .totalPrice(cartItem.getTotalPrice())
                    .build();

            order.getOrderItems().add(orderItem);
        });

        orderRepository.save(order);

        cartService.clearCart(request.cartId());

        return ResponseEntity.ok(new CheckoutDto.Response(order.getId()));
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<Map<String,String>> handleCartNotFound()
    {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT).body(
                Map.of("error","Cart not found.")
        );
    }

    @ExceptionHandler(CartIsEmptyException.class)
    public ResponseEntity<Map<String,String>> handleCartIsEmpty()
    {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT).body(
                Map.of("error","Cart is empty.")
        );
    }
}
