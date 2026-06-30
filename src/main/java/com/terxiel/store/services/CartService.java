package com.terxiel.store.services;

import com.terxiel.store.dtos.CartDTO;
import com.terxiel.store.dtos.CartItemDTO;
import com.terxiel.store.entities.Cart;
import com.terxiel.store.exceptions.CartNotFoundException;
import com.terxiel.store.exceptions.ProductNotFoundException;
import com.terxiel.store.mappers.CartMapper;
import com.terxiel.store.repositories.CartRepository;
import com.terxiel.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@AllArgsConstructor
@Service
public class CartService {
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final ProductRepository productRepository;

    public CartDTO createCart()
    {
        var cart = new Cart();
        cartRepository.save(cart);
        return cartMapper.toDto(cart);
    }

    public CartItemDTO addToCart(UUID cartId, Long productId)
    {
        var cart = getCartEntity(cartId);

        var product = productRepository.findById(productId).orElse(null);
        if(product == null)
        {
            throw new ProductNotFoundException();
        }

        var cartItem = cart.addCartItem(product);

        cartRepository.save(cart);

        return cartMapper.toDto(cartItem);
    }

    public CartDTO getCart(UUID cartId)
    {
        var cart = getCartEntity(cartId);

        return cartMapper.toDto(cart);
    }

    public CartItemDTO updateCartItem(UUID cartId, Long productId, int quantity)
    {
        var cart = getCartEntity(cartId);

        var cartItem = cart.getCartItemByProductId(productId);

        if(cartItem == null)
        {
            throw new ProductNotFoundException();
        }

        cartItem.setQuantity(quantity);
        cartRepository.save(cart);

        return cartMapper.toDto(cartItem);
    }

    public void removeCartItem(UUID cartId, Long productId)
    {
        var cart = getCartEntity(cartId);

        cart.removeCartItem(productId);
        cartRepository.save(cart);
    }

    public void clearCart(UUID cartId) {
        var cart = getCartEntity(cartId);

        cart.clear();
        cartRepository.save(cart);
    }

    private Cart getCartEntity(UUID cartId)
    {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if(cart == null)
        {
            throw new CartNotFoundException();
        }
        return cart;
    }
}
