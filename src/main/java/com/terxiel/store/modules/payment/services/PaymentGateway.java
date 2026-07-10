package com.terxiel.store.modules.payment.services;

import com.terxiel.store.entities.Order;
import com.terxiel.store.modules.payment.dtos.CheckoutSession;
import com.terxiel.store.modules.payment.dtos.PaymentResult;
import com.terxiel.store.modules.payment.dtos.WebhookRequest;

import java.util.Optional;

public interface PaymentGateway {
    CheckoutSession createCheckoutSession(Order order);
    Optional<PaymentResult> parseWebhookRequest(WebhookRequest request);
}
