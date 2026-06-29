package com.terxiel.store.mappers;

import com.terxiel.store.dtos.CartDTO;
import com.terxiel.store.dtos.CartItemDTO;
import com.terxiel.store.entities.Cart;
import com.terxiel.store.entities.CartItem;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CartMapper {
    CartDTO toDto(Cart cart);

    @Mapping(target = "totalPrice", expression = "java(cartItem.getTotalPrice())")
    CartItemDTO toDto(CartItem cartItem);
}