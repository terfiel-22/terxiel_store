package com.terxiel.store.modules.product.dtos;

import java.math.BigDecimal;

public record ProductSummary (
        Long id,
        String name,
        BigDecimal price,
        String description,
        Byte categoryId
){
}
