package com.terxiel.store.seeders;

import com.terxiel.store.entities.Category;
import com.terxiel.store.entities.Product;
import com.terxiel.store.repositories.CategoryRepository;
import com.terxiel.store.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import net.datafaker.Faker;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@Service
@AllArgsConstructor
public class ProductSeeder implements SeederInterface {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final Faker faker = new Faker();

    @Override
    @Transactional
    public void populate() {
        List<Category> categories;

        if(productRepository.count() == 0)
        {
            if(categoryRepository.count() == 0)
            {
                Set<String> uniqueCategoryNames = new HashSet<>();

                // Gather unique names to prevent MySQL validation duplicate errors
                while (uniqueCategoryNames.size() < 5) {
                    uniqueCategoryNames.add(faker.commerce().department());
                }

                // Save to database
                categories = uniqueCategoryNames.stream().map(Category::new).toList();
                categoryRepository.saveAll(categories);

                System.out.println("✅ Created 5 categories.");
            } else {
                categories = categoryRepository.findAll();
            }

            long categoriesCount = categoryRepository.count();
            List<Product> mockProducts = new ArrayList<>();

            for (int i = 0; i < 10; i++) {
                // Get random category from the categories
                long randomId = ThreadLocalRandom.current().nextLong(categoriesCount);
                var category = categories.get((int) randomId);

                // Create a product
                var product = Product.builder()
                        .name(faker.commerce().productName())
                        .price(BigDecimal.valueOf(faker.number().randomDouble(2, 5, 2000)))
                        .description(faker.lorem().sentence(12))
                        .category(category)
                        .build();
                mockProducts.add(product);
            }

            // Save products to the database
            productRepository.saveAll(mockProducts);
            System.out.println("✅ Created 10 products.");
        }
    }
}
