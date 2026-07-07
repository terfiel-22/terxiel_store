package com.terxiel.store.controllers;

import com.terxiel.store.dtos.CheckoutDto;
import com.terxiel.store.dtos.ErrorDTO;
import com.terxiel.store.entities.Order;
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

        // Initialize order
        var order = Order.fromCart(cart,user);

        orderRepository.save(order);

        cartService.clearCart(request.cartId());

        return ResponseEntity.ok(new CheckoutDto.Response(order.getId()));
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleCartNotFound()
    {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT).body(
                new ErrorDTO("Cart not found.")
        );
    }

    @ExceptionHandler(CartIsEmptyException.class)
    public ResponseEntity<ErrorDTO> handleCartIsEmpty()
    {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT).body(
                new ErrorDTO("Cart is empty.")
        );
    }
}
