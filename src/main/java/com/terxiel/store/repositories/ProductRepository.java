package com.terxiel.store.repositories;

import com.terxiel.store.entities.Product;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Long> {
    // Strings
    List<Product> findProductByName(String name);
    List<Product> findProductByNameLike(String name);
    List<Product> findProductByNameNotLike(String name);
    List<Product> findProductByNameContaining(String name);
    List<Product> findProductByNameStartingWith(String name);
    List<Product> findProductByNameEndingWith(String name);
    List<Product> findProductByNameEndingWithIgnoreCase(String name);

    // Numbers
    List<Product> findProductByPrice(BigDecimal price);
    List<Product> findProductByPriceGreaterThan(BigDecimal price);
    List<Product> findProductByPriceLessThan(BigDecimal price);
    List<Product> findProductByPriceBetween(BigDecimal min, BigDecimal max);

    // Null
    List<Product> findProductByDescriptionNull();
    List<Product> findProductByDescriptionNotNull();

    // Multiple conditions
    List<Product> findProductByDescriptionNullAndNameNull();

    // Sort (OrderBy)
    List<Product> findProductByNameOrderByPriceDesc(String name);

    // Limit (Top/First)
    List<Product> findTop5ByName(String name);
    List<Product> findFirst5ByName(String name);
}