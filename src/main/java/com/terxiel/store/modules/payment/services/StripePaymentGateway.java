package com.terxiel.store.modules.payment.services;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import com.terxiel.store.entities.Order;
import com.terxiel.store.entities.OrderItem;
import com.terxiel.store.entities.OrderStatus;
import com.terxiel.store.modules.payment.exceptions.PaymentException;
import com.terxiel.store.modules.payment.dtos.CheckoutSession;
import com.terxiel.store.modules.payment.dtos.PaymentResult;
import com.terxiel.store.modules.payment.dtos.WebhookRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StripePaymentGateway implements PaymentGateway{
    @Value("${websiteUrl}")
    private String websiteUrl;

    @Value("${stripe.webhookSecretKey}")
    private String webhookSecretKey;

    @Override
    public CheckoutSession createCheckoutSession(Order order) {
        try {
            var builder = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(websiteUrl+"/checkout-success?orderId="+order.getId())
                    .setCancelUrl(websiteUrl+"/checkout-cancel")
                    .setPaymentIntentData(createPaymentIntent(order));

            order.getOrderItems().forEach(item->{
                var lineItem = createLineItem(item);

                builder.addLineItem(lineItem);
            });

            var session = Session.create(builder.build());

            return new CheckoutSession(session.getUrl());

        } catch (StripeException ex) {
            throw new PaymentException();
        }
    }

    @Override
    public Optional<PaymentResult> parseWebhookRequest(WebhookRequest request) {
        try {
            var payload = request.payload();
            var signature = request.headers().get("Stripe-Signature");
            var event = Webhook.constructEvent(payload,signature,webhookSecretKey);

            return switch (event.getType())
            {
                case "payment_intent.succeeded" ->
                        Optional.of(new PaymentResult(extractOrderId(event),OrderStatus.PAID));
                case "payment_intent.payment_failed" ->
                        Optional.of(new PaymentResult(extractOrderId(event),OrderStatus.FAILED));
                default -> Optional.empty();
            };
        } catch (SignatureVerificationException e) {
            throw new PaymentException("Invalid signature.");
        }
    }

    private SessionCreateParams.LineItem createLineItem(OrderItem item) {
        return SessionCreateParams.LineItem.builder()
                .setQuantity(Long.valueOf(item.getQuantity()))
                .setPriceData(
                        createPriceData(item)
                ).build();
    }

    private SessionCreateParams.LineItem.PriceData createPriceData(OrderItem item) {
        return SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("PHP")
                .setUnitAmountDecimal(item.getUnitAmountDecimal())
                .setProductData(
                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                .setName(item.getProduct().getName())
                                .build()
                )
                .build();
    }

    private Long extractOrderId(Event event)
    {
        var stripeObject = event.getDataObjectDeserializer().getObject().orElseThrow(
                ()-> new PaymentException("Could not deserialize Stripe event. Check the SDK and API version.")
        );
        var paymentIntent = (PaymentIntent) stripeObject;

        return Long.valueOf(paymentIntent.getMetadata().get("order_id"));
    }

    private SessionCreateParams.PaymentIntentData createPaymentIntent(Order order) {
        return SessionCreateParams.PaymentIntentData.builder()
                .putMetadata("order_id", order.getId().toString()).build();
    }
}
