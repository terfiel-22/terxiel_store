package com.terxiel.store;

import com.terxiel.store.entities.Address;
import com.terxiel.store.entities.Profile;
import com.terxiel.store.entities.Tag;
import com.terxiel.store.entities.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.time.LocalDate;

@SpringBootApplication
public class StoreApplication {

    static void main(String[] args) {
//        ConfigurableApplicationContext applicationContext = SpringApplication.run(StoreApplication.class, args);

        var user = User.builder()
                .name("Terxiel Kenway")
                .email("terxiel@gmail.com")
                .password("!password")
                .build();

        var profile = Profile.builder()
                .bio("He/Him")
                .phoneNumber("09847264928")
                .dateOfBirth(LocalDate.of(2003,10,7))
                .loyaltyPoints(100)
                .build();

        user.addProfile(profile);

        System.out.println(user);
    }
}
