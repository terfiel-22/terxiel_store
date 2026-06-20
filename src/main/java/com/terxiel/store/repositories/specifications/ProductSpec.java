package com.terxiel.store.repositories.specifications;

import com.terxiel.store.entities.Category;
import com.terxiel.store.entities.Product;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.PredicateSpecification;

import java.math.BigDecimal;

public class ProductSpec {
    // Filtering products by their name.
    public static PredicateSpecification<Product> hasName(String name) {
        return (root,cb) -> cb.like(root.get("name"),"%"+name+"%");
    }

    // Filtering products where product price greater than or equal to given price
    public static PredicateSpecification<Product> hasPriceGreaterThanOrEqualTo(BigDecimal price) {
        return (root,cb) -> cb.greaterThanOrEqualTo(root.get("price"),price);
    }

    // Filtering products where product price less than or equal to given price
    public static PredicateSpecification<Product> hasPriceLessThanOrEqualTo(BigDecimal price) {
        return (root,cb) -> cb.lessThanOrEqualTo(root.get("price"),price);
    }

    // Filtering products by category
    public static PredicateSpecification<Product> hasCategoryEqualTo(String categoryName) {
        return (root,cb) -> {

            // Join the Product entity with its Category field
            // Replace "category" with the exact field name defined in your Product class
            Join<Product, Category> categoryJoin = root.join("category");
            return cb.equal(categoryJoin.get("name"),categoryName);
        };
    }
}
