package com.terxiel.store.repositories;

import com.terxiel.store.dtos.ProductSummary;
import com.terxiel.store.dtos.ProductSummaryRecord;
import com.terxiel.store.entities.Category;
import com.terxiel.store.entities.Product;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

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

    // Find products whose prices are within the given range and sort by name.
    List<Product> findByPriceBetweenOrderByName(BigDecimal min, BigDecimal max);

    // SQL
    //@Query(value = "SELECT * FROM products p WHERE p.price BETWEEN :min AND :max ORDER BY p.name", nativeQuery = true)
    // JPQL
    @Query("SELECT p FROM Product p JOIN p.category WHERE p.price BETWEEN :min AND :max ORDER BY p.name")
    List<Product> findProducts(@Param("min") BigDecimal min, @Param("max") BigDecimal max);

    @Query("select count(p) from Product p where p.price between :min and :max")
    long countProducts(@Param("min") BigDecimal min, @Param("max") BigDecimal max);

    @Modifying
    @Query("UPDATE Product p SET p.price=:new_price WHERE p.category.id = :category_id")
    void updatePriceByCategory(@Param("new_price") BigDecimal newPrice, @Param("category_id") Byte categoryId);

    @Query("select new com.terxiel.store.dtos.ProductSummaryRecord(p.id,p.name) from Product p where p.category = :category")
    List<ProductSummaryRecord> findByCategory(@Param("category") Category category);
}