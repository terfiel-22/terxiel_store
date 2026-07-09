package com.terxiel.store.services;

import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.terxiel.store.dtos.CheckoutDto;
import com.terxiel.store.entities.Order;
import com.terxiel.store.exceptions.CartIsEmptyException;
import com.terxiel.store.exceptions.CartNotFoundException;
import com.terxiel.store.repositories.CartRepository;
import com.terxiel.store.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CheckoutService {

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final AuthService authService;
    private final CartService cartService;

    @Value("${websiteUrl}")
    private String websiteUrl;

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
        var builder = SessionCreateParams.builder()
                            .setMode(SessionCreateParams.Mode.PAYMENT)
                            .setSuccessUrl(websiteUrl+"/checkout-success?orderId="+order.getId())
                            .setCancelUrl(websiteUrl+"/checkout-cancel");

        order.getOrderItems().forEach(item->{
            var lineItem = SessionCreateParams.LineItem.builder()
                    .setQuantity(Long.valueOf(item.getQuantity()))
                    .setPriceData(
                            SessionCreateParams.LineItem.PriceData.builder()
                                    .setCurrency("PHP")
                                    .setUnitAmountDecimal(item.getUnitAmountDecimal())
                                    .setProductData(
                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                    .setName(item.getProduct().getName())
                                                    .build()
                                    )
                                    .build()
                    ).build();

            builder.addLineItem(lineItem);
        });

        try {
            var session = Session.create(builder.build());

            cartService.clearCart(request.cartId());

            return new CheckoutDto.Response(order.getId(), session.getUrl());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new CheckoutDto.Response(order.getId(), null);
        }
    }
}
