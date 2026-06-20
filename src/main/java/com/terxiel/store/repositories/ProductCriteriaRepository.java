package com.terxiel.store.repositories;

import com.terxiel.store.entities.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductCriteriaRepository {
    List<Product> findProductsByCriteria(String name, BigDecimal minPrice, BigDecimal maxPrice);

    List<Product> findProductsByCategory(String category);
}