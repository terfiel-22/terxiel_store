package com.terxiel.store.startup;

// import com.terxiel.store.seeders.ProductSeeder;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


@Component
@Profile({"dev", "test"})
@AllArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {
    // private final ProductSeeder productSeeder;

    @Override
    public void run(String @NonNull ... args) {
        // productSeeder.populate();
    }
}
