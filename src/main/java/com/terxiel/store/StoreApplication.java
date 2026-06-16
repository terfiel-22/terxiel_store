package com.terxiel.store;

import com.terxiel.store.entities.Product;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;

@SpringBootApplication
public class StoreApplication {

    static void main(String[] args) {
//        ConfigurableApplicationContext applicationContext = SpringApplication.run(StoreApplication.class, args);

        var product = Product.builder()
                .name("Red Dress")
                .price(BigDecimal.valueOf(200.50D))
                .build();

        product.addCategory("Clothes");

        System.out.println(product);
    }
}
