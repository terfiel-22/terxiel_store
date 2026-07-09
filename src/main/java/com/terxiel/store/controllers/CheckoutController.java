package com.terxiel.store.controllers;

import com.stripe.exception.StripeException;
import com.terxiel.store.dtos.CheckoutDto;
import com.terxiel.store.dtos.ErrorDTO;
import com.terxiel.store.exceptions.CartIsEmptyException;
import com.terxiel.store.exceptions.CartNotFoundException;
import com.terxiel.store.services.CheckoutService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("checkout")
public class CheckoutController {

    private final CheckoutService checkoutService;

    @PostMapping
    public ResponseEntity<?> checkout(
            @Valid @RequestBody CheckoutDto.Request request
    )
    {
        try {
            return ResponseEntity.ok(checkoutService.checkout(request));
        } catch (StripeException e) {
            return ResponseEntity.internalServerError()
                    .body(new ErrorDTO("Error creating checkout session."));
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
}
