package com.terxiel.store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class StoreApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(StoreApplication.class, args);

        var userService = applicationContext.getBean(UserService.class);

        var newUser = new User(
                1234,
                "terxiel@gmail.com",
                "hello@springboot",
                "Terxiel Kenway"
        );

        userService.registerUser(newUser);
    }

}
