package com.terxiel.store;

import com.terxiel.store.repositories.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class StoreApplication {

    static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(StoreApplication.class, args);

        var userRepository = applicationContext.getBean(UserRepository.class);
        userRepository.deleteById(1L);
    }
}
