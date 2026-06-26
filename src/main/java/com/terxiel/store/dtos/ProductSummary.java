package com.terxiel.store.dtos;

import java.math.BigDecimal;

public record ProductSummary (
        Long id,
        String name,
        BigDecimal price,
        String description,
        String category
){
}
