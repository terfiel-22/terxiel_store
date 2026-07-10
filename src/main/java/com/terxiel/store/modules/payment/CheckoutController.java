package com.terxiel.store.modules.payment;

import com.terxiel.store.modules.payment.dtos.CheckoutDto;
import com.terxiel.store.shared.dtos.ErrorDTO;
import com.terxiel.store.modules.cart.exceptions.CartIsEmptyException;
import com.terxiel.store.modules.cart.exceptions.CartNotFoundException;
import com.terxiel.store.modules.payment.exceptions.PaymentException;
import com.terxiel.store.modules.payment.services.CheckoutService;
import com.terxiel.store.modules.payment.dtos.WebhookRequest;
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

    private final CheckoutService checkoutService;

    @PostMapping
    public CheckoutDto.Response checkout(
            @Valid @RequestBody CheckoutDto.Request request
    )
    {
        return checkoutService.checkout(request);
    }

    @PostMapping("/webhook")
    public void webhook(
        @RequestHeader Map<String, String> headers,
        @RequestBody String payload
    )
    {
        checkoutService.handleWebhookEvent(new WebhookRequest(headers,payload));
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
    public ResponseEntity<ErrorDTO> handlePaymentException(PaymentException ex)
    {
        return ResponseEntity.internalServerError()
                .body(new ErrorDTO(ex.getMessage().isEmpty() ? "Error creating checkout session." : ex.getMessage()));
    }

}
