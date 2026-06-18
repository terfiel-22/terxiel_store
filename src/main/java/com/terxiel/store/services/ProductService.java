package com.terxiel.store.services;

import com.terxiel.store.entities.Product;
import com.terxiel.store.repositories.CategoryRepository;
import com.terxiel.store.repositories.ProductRepository;
import com.terxiel.store.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
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
        productRepository.findProductByName("IPhone17");
        productRepository.findProductByNameLike("IPhone17");
        productRepository.findProductByNameNotLike("IPhone17");
        productRepository.findProductByNameContaining("IPhone17");
        productRepository.findProductByNameStartingWith("IPhone17");
        productRepository.findProductByNameEndingWith("IPhone17");
        productRepository.findProductByNameEndingWithIgnoreCase("IPhone17");

        productRepository.findProductByPrice(BigDecimal.valueOf(50000));
        productRepository.findProductByPriceGreaterThan(BigDecimal.valueOf(50000));
        productRepository.findProductByPriceLessThan(BigDecimal.valueOf(50000));
        productRepository.findProductByPriceBetween(BigDecimal.valueOf(50000),BigDecimal.valueOf(60000));

        productRepository.findProductByDescriptionNull();
        productRepository.findProductByDescriptionNotNull();

        productRepository.findProductByDescriptionNullAndNameNull();

        productRepository.findProductByNameOrderByPriceDesc("IPhone17");

        productRepository.findTop5ByName("IPhone17");
        productRepository.findFirst5ByName("IPhone17");
    }
}
