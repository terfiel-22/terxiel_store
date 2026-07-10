package com.terxiel.store.modules.product.dtos;
import java.math.BigDecimal;

/**
 * Projection for {@link com.terxiel.store.entities.Product}
 */
public record UpdateProductRequest(
        String name,
        String description,
        BigDecimal price,
        Byte categoryId
) {
}