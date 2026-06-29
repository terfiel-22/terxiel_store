package com.terxiel.store.controllers;

import com.terxiel.store.dtos.CartDTO;
import com.terxiel.store.entities.Cart;
import com.terxiel.store.mappers.CartMapper;
import com.terxiel.store.repositories.CartRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriComponentsBuilder;

@AllArgsConstructor
@RestController
@RequestMapping("carts")
class CartController {
    private CartRepository cartRepository;
    private CartMapper cartMapper;

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
}
