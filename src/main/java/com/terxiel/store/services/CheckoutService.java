package com.terxiel.store.services;

import com.terxiel.store.dtos.CheckoutDto;
import com.terxiel.store.entities.Order;
import com.terxiel.store.exceptions.CartIsEmptyException;
import com.terxiel.store.exceptions.CartNotFoundException;
import com.terxiel.store.repositories.CartRepository;
import com.terxiel.store.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CheckoutService {

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final AuthService authService;
    private final CartService cartService;

    public CheckoutDto.Response checkout(CheckoutDto.Request request)
    {
        // Get cart from request
        var cart = cartRepository.getCartWithItems(request.cartId()).orElseThrow(CartNotFoundException::new);
        if(cart.isEmpty())
        {
            throw new CartIsEmptyException();
        }

        // Get current user
        var user = authService.getCurrentUser();

        // Initialize order
        var order = Order.fromCart(cart,user);

        orderRepository.save(order);

        cartService.clearCart(request.cartId());

        return new CheckoutDto.Response(order.getId());
    }
}
