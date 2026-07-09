package com.terxiel.store.controllers;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import com.terxiel.store.dtos.CheckoutDto;
import com.terxiel.store.dtos.ErrorDTO;
import com.terxiel.store.entities.OrderStatus;
import com.terxiel.store.exceptions.CartIsEmptyException;
import com.terxiel.store.exceptions.CartNotFoundException;
import com.terxiel.store.exceptions.PaymentException;
import com.terxiel.store.repositories.OrderRepository;
import com.terxiel.store.services.CheckoutService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("checkout")
public class CheckoutController {

    private final CheckoutService checkoutService;

    @Value("${stripe.webhookSecretKey}")
    private String webhookSecretKey;
    private final OrderRepository orderRepository;

    @PostMapping
    public CheckoutDto.Response checkout(
            @Valid @RequestBody CheckoutDto.Request request
    )
    {
        return checkoutService.checkout(request);
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> webhook(
        @RequestHeader("Stripe-Signature") String signature,
        @RequestBody String payload
    )
    {
        try {
            var event = Webhook.constructEvent(payload,signature,webhookSecretKey);
            var stripeObject = event.getDataObjectDeserializer().getObject().orElse(null);
            System.out.println(event.getType());
            switch (event.getType())
            {
                case "payment_intent.succeeded" -> {
                    var paymentIntent = (PaymentIntent) stripeObject;
                    if(paymentIntent != null)
                    {
                        var orderId = paymentIntent.getMetadata().get("order_id");
                        var order = orderRepository.findById(Long.valueOf(orderId)).orElseThrow();
                        order.setStatus(OrderStatus.PAID);
                        orderRepository.save(order);
                    }
                }
                case "payment_intent.failed" -> {
                    // Update order status (FAILED)
                }
            }

            return ResponseEntity.ok().build();
        } catch (SignatureVerificationException e) {
            return ResponseEntity.badRequest().build();
        }
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

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<ErrorDTO> handlePaymentException()
    {
        return ResponseEntity.internalServerError()
                .body(new ErrorDTO("Error creating checkout session."));
    }

}
