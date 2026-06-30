package com.terxiel.store.controllers;

import com.terxiel.store.dtos.AddItemToCartRequest;
import com.terxiel.store.dtos.CartDTO;
import com.terxiel.store.dtos.CartItemDTO;
import com.terxiel.store.entities.Cart;
import com.terxiel.store.entities.CartItem;
import com.terxiel.store.mappers.CartMapper;
import com.terxiel.store.repositories.CartRepository;
import com.terxiel.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Objects;
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

        var cartItem = cart.getCartItems().stream()
                .filter(
                        item-> Objects.equals(item.getProduct().getId(), request.productId())
                ).findFirst()
                .orElse(null);

        if(cartItem != null)
        {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
        } else {
            cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(1);
            cartItem.setCart(cart);
            cart.getCartItems().add(cartItem);
        }

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
}
