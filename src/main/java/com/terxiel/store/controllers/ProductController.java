package com.terxiel.store.controllers;

import com.terxiel.store.dtos.ProductSummary;
import com.terxiel.store.dtos.UpdateProductRequest;
import com.terxiel.store.mappers.ProductMapper;
import com.terxiel.store.repositories.CategoryRepository;
import com.terxiel.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@AllArgsConstructor
@RequestMapping("products")
public class ProductController {
    private final CategoryRepository categoryRepository;
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

    @PostMapping
    public ResponseEntity<ProductSummary> createProduct(
            @RequestBody ProductSummary productSummary,
            UriComponentsBuilder uriComponentsBuilder
    )
    {
        var category = categoryRepository.findById(productSummary.categoryId()).orElse(null);
        if(category == null)
        {
            return ResponseEntity.unprocessableContent().build();
        }

        var product = productMapper.toEntity(productSummary);
        product.setCategory(category);
        productRepository.save(product);

        var productDto = productMapper.toDto(product);
        var uri = uriComponentsBuilder.path("/products/{id}").buildAndExpand(productDto.id()).toUri();

        return ResponseEntity.created(uri).body(productDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductSummary> updateProduct(
            @PathVariable Long id,
            @RequestBody UpdateProductRequest updateProductRequest
    )
    {
        var product = productRepository.findById(id).orElse(null);
        if(product == null)
        {
            return ResponseEntity.notFound().build();
        }

        var category = categoryRepository.findById(updateProductRequest.categoryId()).orElse(null);
        if(category == null)
        {
            return ResponseEntity.unprocessableContent().build();
        }

        productMapper.update(updateProductRequest,product);

        product.setCategory(category);
        productRepository.save(product);

        var productDto = productMapper.toDto(product);
        return ResponseEntity.ok(productDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id)
    {
        var product = productRepository.findById(id).orElse(null);
        if(product == null)
        {
            return ResponseEntity.notFound().build();
        }

        productRepository.delete(product);
        return ResponseEntity.noContent().build();
    }
}
