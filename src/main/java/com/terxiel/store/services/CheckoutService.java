package com.terxiel.store.services;

import com.terxiel.store.dtos.CheckoutDto;
import com.terxiel.store.entities.Order;
import com.terxiel.store.exceptions.CartIsEmptyException;
import com.terxiel.store.exceptions.CartNotFoundException;
import com.terxiel.store.exceptions.PaymentException;
import com.terxiel.store.repositories.CartRepository;
import com.terxiel.store.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@AllArgsConstructor
@Service
public class CheckoutService {

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final AuthService authService;
    private final CartService cartService;
    private final PaymentGateway paymentGateway;


    @Transactional
    public CheckoutDto.Response checkout(CheckoutDto.Request request) {
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

        // Create a checkout session
        try {
            var session = paymentGateway.createCheckoutSession(order);

            cartService.clearCart(request.cartId());

            return new CheckoutDto.Response(order.getId(), session.checkoutUrl());
        } catch (PaymentException ex) {
            orderRepository.delete(order);
            throw ex;
        }
    }

    public void handleWebhookEvent(WebhookRequest request)
    {
        paymentGateway.parseWebhookRequest(request).ifPresent(result->{
            var order = orderRepository.findById(result.orderId()).orElseThrow();
            order.setStatus(result.paymentStatus());
            orderRepository.save(order);
        });
    }
}
