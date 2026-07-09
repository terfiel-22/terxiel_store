package com.terxiel.store.services;

import com.terxiel.store.entities.Order;

public interface PaymentGateway {
    CheckoutSession createCheckoutSession(Order order);
}
