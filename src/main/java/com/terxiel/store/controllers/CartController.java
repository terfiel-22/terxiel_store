package com.terxiel.store.controllers;

import com.terxiel.store.dtos.*;
import com.terxiel.store.exceptions.CartNotFoundException;
import com.terxiel.store.exceptions.ProductNotFoundException;
import com.terxiel.store.services.CartService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("carts")
class CartController {
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<CartDTO> createCart(
            UriComponentsBuilder uriComponentsBuilder
    )
    {
        var cartDTO = cartService.createCart();
        var uri = uriComponentsBuilder.path("/carts/{id}").buildAndExpand(cartDTO.id()).toUri();
        return ResponseEntity.created(uri).body(cartDTO);
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<?> addToCart(
            @PathVariable UUID cartId,
            @RequestBody AddItemToCartRequest request
    )
    {
        var cartItemDTO = cartService.addToCart(cartId,request.productId());
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDTO);
    }

    @GetMapping("/{cartId}")
    public CartDTO getCart(@PathVariable UUID cartId)
    {
        return cartService.getCart(cartId);
    }

    @PutMapping("/{cartId}/items/{productId}")
    public CartItemDTO updateCartItem(
            @PathVariable UUID cartId,
            @PathVariable Long productId,
            @Valid @RequestBody UpdateCartItemRequest request
            )
    {
        return cartService.updateCartItem(cartId,productId,request.quantity());
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> removeCartItem(
            @PathVariable UUID cartId,
            @PathVariable Long productId
    )
    {
        cartService.removeCartItem(cartId,productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cartId}/items")
    public ResponseEntity<?> clearCart(
            @PathVariable UUID cartId
    )
    {
        cartService.clearCart(cartId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleCartNotFoundException()
    {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorDTO("Cart not found.")
        );
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleProductNotFoundException()
    {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT).body(
               new ErrorDTO("Product not found in the cart.")
        );
    }
}
