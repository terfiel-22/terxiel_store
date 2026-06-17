package com.terxiel.store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class StoreApplication {

    static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(StoreApplication.class, args);
    }
}
