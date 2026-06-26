package com.terxiel.store.controllers;

import com.terxiel.store.dtos.ProductSummary;
import com.terxiel.store.mappers.ProductMapper;
import com.terxiel.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("products")
public class ProductController {
    private ProductRepository productRepository;
    private ProductMapper productMapper;

    @GetMapping
    public Iterable<ProductSummary> getProducts(@RequestParam(required = false, name = "categoryId") Byte categoryId)
    {
        if(categoryId != null)
        {
            return productRepository.findByCategoryId(categoryId).stream().map(productMapper::toDto).toList();
        }

        return productRepository.findAllProducts().stream().map(productMapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductSummary> getProduct(@PathVariable Long id)
    {
        var product = productRepository.findSingleProductById(id).orElse(null);
        if(product == null)
        {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productMapper.toDto(product));
    }
}
