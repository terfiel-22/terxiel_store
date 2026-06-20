package com.terxiel.store;

import com.terxiel.store.services.ProductService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class StoreApplication {

    static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(StoreApplication.class, args);

        var service = applicationContext.getBean(ProductService.class);
        service.findProductsBySpecifications(null,null,null,"Smartphone");
    }
}
