package com.terxiel.store.controllers;

import com.terxiel.store.dtos.AddItemToCartRequest;
import com.terxiel.store.dtos.CartDTO;
import com.terxiel.store.dtos.CartItemDTO;
import com.terxiel.store.dtos.UpdateCartItemRequest;
import com.terxiel.store.entities.Cart;
import com.terxiel.store.mappers.CartMapper;
import com.terxiel.store.repositories.CartRepository;
import com.terxiel.store.repositories.ProductRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("carts")
class CartController {
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;

    @PostMapping
    public ResponseEntity<CartDTO> createCart(
            UriComponentsBuilder uriComponentsBuilder
    )
    {
        var cart = new Cart();
        cartRepository.save(cart);
        var cartDTO = cartMapper.toDto(cart);
        var uri = uriComponentsBuilder.path("/carts/{id}").buildAndExpand(cartDTO.id()).toUri();

        return ResponseEntity.created(uri).body(cartDTO);
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartItemDTO> addToCart(
            @PathVariable UUID cartId,
            @RequestBody AddItemToCartRequest request
    )
    {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if(cart == null)
        {
            return ResponseEntity.notFound().build();
        }

        var product = productRepository.findById(request.productId()).orElse(null);
        if(product == null)
        {
            return ResponseEntity.unprocessableContent().build();
        }

        var cartItem = cart.addCartItem(product);

        cartRepository.save(cart);
        var cartItemDTO = cartMapper.toDto(cartItem);

        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDTO);
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<CartDTO> getACart(@PathVariable UUID cartId)
    {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if(cart == null)
        {
            return ResponseEntity.notFound().build();
        }

        var cartDto = cartMapper.toDto(cart);

        return ResponseEntity.ok().body(cartDto);
    }

    @PutMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> updateCartItem(
            @PathVariable UUID cartId,
            @PathVariable Long productId,
            @Valid @RequestBody UpdateCartItemRequest request
            )
    {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if(cart == null)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error","Cart not found.")
            );
        }

        var cartItem = cart.getCartItemByProductId(productId);

        if(cartItem == null)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error","Product was not found in the cart.")
            );
        }

        cartItem.setQuantity(request.quantity());
        cartRepository.save(cart);

        return ResponseEntity.ok(cartMapper.toDto(cartItem));
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> removeCartItem(
            @PathVariable UUID cartId,
            @PathVariable Long productId
    )
    {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if(cart == null)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error","Cart not found.")
            );
        }

        cart.removeCartItem(productId);

        cartRepository.save(cart);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cartId}/items")
    public ResponseEntity<?> clearCart(
            @PathVariable UUID cartId
    )
    {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if(cart == null)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error","Cart not found.")
            );
        }

        cart.clear();
        cartRepository.save(cart);

        return ResponseEntity.noContent().build();
    }
}
