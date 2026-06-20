package com.terxiel.store.services;

import com.terxiel.store.entities.Category;
import com.terxiel.store.entities.Product;
import com.terxiel.store.repositories.CategoryRepository;
import com.terxiel.store.repositories.ProductRepository;
import com.terxiel.store.repositories.UserRepository;
import com.terxiel.store.repositories.specifications.ProductSpec;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class ProductService {
    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;
    private UserRepository userRepository;

    public void createProductAndCategory()
    {
        var product = Product.builder()
                .name("IPhone17")
                .price(BigDecimal.valueOf(50000.00))
                .description("An Apple Smartphone")
                .build();

        product.addCategory("Smartphone");
        productRepository.save(product);
    }

    @Transactional
    public void createProductWithExistingCategory()
    {
        var category = categoryRepository.findById((byte) 1).orElseThrow(()->new RuntimeException("Category not found"));

        var product = Product.builder()
                .name("Xiaomi")
                .price(BigDecimal.valueOf(50000.00))
                .description("An Android Smartphone")
                .build();

        product.setCategory(category);

        productRepository.save(product);
    }

    @Transactional
    public void addAllProductsToUserWishlist()
    {
        var user = userRepository.findById(16L).orElseThrow(()-> new RuntimeException("User not found"));
        var products = productRepository.findAll();
        products.forEach(user::addWishlist); // equivalent to product->user.addWishlist(product)
        userRepository.save(user);
    }

    public void deleteProduct()
    {
        productRepository.deleteById(7L);
    }

    public void fetchProductsByName()
    {
//        productRepository.findProductByName("IPhone17");
//        productRepository.findProductByNameLike("IPhone17");
//        productRepository.findProductByNameNotLike("IPhone17");
//        productRepository.findProductByNameContaining("IPhone17");
//        productRepository.findProductByNameStartingWith("IPhone17");
//        productRepository.findProductByNameEndingWith("IPhone17");
//        productRepository.findProductByNameEndingWithIgnoreCase("IPhone17");
//
//        productRepository.findProductByPrice(BigDecimal.valueOf(50000));
//        productRepository.findProductByPriceGreaterThan(BigDecimal.valueOf(50000));
//        productRepository.findProductByPriceLessThan(BigDecimal.valueOf(50000));
//        productRepository.findProductByPriceBetween(BigDecimal.valueOf(50000),BigDecimal.valueOf(60000));
//
//        productRepository.findProductByDescriptionNull();
//        productRepository.findProductByDescriptionNotNull();
//
//        productRepository.findProductByDescriptionNullAndNameNull();
//
//        productRepository.findProductByNameOrderByPriceDesc("IPhone17");
//
//        productRepository.findTop5ByName("IPhone17");
//        productRepository.findFirst5ByName("IPhone17");

        productRepository.findProducts(BigDecimal.valueOf(10000),BigDecimal.valueOf(70000)).forEach(System.out::println);
//        long count = productRepository.countProducts(BigDecimal.valueOf(10000),BigDecimal.valueOf(70000));
//        System.out.println(count);
    }

    @Transactional
    public void updateProductPrices()
    {
        productRepository.updatePriceByCategory(BigDecimal.valueOf(500),(byte) 1);
    }

    public void fetchProducts()
    {
        var category = new Category("Smartphone");
        category.setId((byte)1);
        productRepository.findByCategory(category).forEach(System.out::println);
    }

    @Transactional
    public void fetchProductsRange()
    {
        productRepository.findProductsByPrice(BigDecimal.valueOf(10000),BigDecimal.valueOf(70000)).forEach(System.out::println);
    }

    @Transactional
    public void fetchProductsLike()
    {
        var product = new Product();
        product.setName("phone"); // name should contain "phone" keyword

        // Example matcher
        var matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        var example = Example.of(product,matcher);
        var products = productRepository.findAll(example);
        products.forEach(System.out::println);
    }

    @Transactional
    public void fetchProductByCriteria()
    {
        var products = productRepository.findProductsByCriteria("phone", null,null);
        products.forEach(System.out::println);
    }

    @Transactional
    public void findProductsBySpecifications(String name, BigDecimal minPrice, BigDecimal maxPrice)
    {
        Specification<Product> spec = Specification.unrestricted();

        if(name != null)
        {
            spec = spec.and(ProductSpec.hasName(name));
        }
        if(minPrice != null)
        {
            spec = spec.and(ProductSpec.hasPriceGreaterThanOrEqualTo(minPrice));
        }
        if(maxPrice != null)
        {
            spec = spec.and(ProductSpec.hasPriceLessThanOrEqualTo(maxPrice));
        }

        var products = productRepository.findAll(spec);
        products.forEach(System.out::println);
    }

    @Transactional
    public void fetchSortedProducts()
    {
        // Sort by name, then by price descending.
        var sort = Sort.by("name").and(Sort.by("price").descending());

        var products = productRepository.findAll(sort);
        products.forEach(System.out::println);
    }

    @Transactional
    public void fetchPaginatedProducts(int pageNumber, int size)
    {
        PageRequest pageRequest = PageRequest.of(pageNumber,size);

        Page<Product> page = productRepository.findAll(pageRequest);
        var products = page.getContent();
        products.forEach(System.out::println);

        var totalPages = page.getTotalPages();
        var totalElements = page.getTotalElements();
        System.out.println("Total Pages: "+totalPages);
        System.out.println("Total Elements: "+totalElements);
    }
}
