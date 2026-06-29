package com.terxiel.store.dtos;

import java.math.BigDecimal;

/**
 * DTO for {@link com.terxiel.store.entities.Product}
 */
public record CartProductDTO (
        Long id,
        String name,
        BigDecimal price
){
}