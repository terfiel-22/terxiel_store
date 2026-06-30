package com.terxiel.store.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "date_created", insertable = false, updatable = false)
    private LocalDate dateCreated;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.MERGE, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<CartItem> cartItems = new LinkedHashSet<>();

    public BigDecimal getTotalPrice()
    {
        return cartItems.stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO,BigDecimal::add);
    }

    public CartItem getCartItemByProductId(Long productId)
    {
        return cartItems.stream()
                .filter(
                        item-> Objects.equals(item.getProduct().getId(), productId)
                ).findFirst()
                .orElse(null);
    }

    public CartItem addCartItem(Product product)
    {
        var cartItem = getCartItemByProductId(product.getId());

        if(cartItem != null)
        {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
        } else {
            cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(1);
            cartItem.setCart(this);
            getCartItems().add(cartItem);
        }

        return cartItem;
    }

    public void removeCartItem(Long productId)
    {
        var cartItem = getCartItemByProductId(productId);
        if(cartItem != null)
        {
            getCartItems().remove(cartItem);
            cartItem.setCart(null);
        }
    }

    public void clear()
    {
        cartItems.clear();
    }
}