package com.terxiel.store.mappers;

import com.terxiel.store.dtos.CartDTO;
import com.terxiel.store.entities.Cart;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CartMapper {
    Cart toEntity(CartDTO cartDTO);
    CartDTO toDto(Cart cart);
    Cart update(CartDTO cartDTO, @MappingTarget Cart cart);
}