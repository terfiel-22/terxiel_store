package com.terxiel.store.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
    {
        http
            // Stateless session (token-based authentication)
            .sessionManagement(c->c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // Disable CSRF
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(
                    a->a
                            // Allows carts endpoints to the public
                            .requestMatchers("/carts/**").permitAll()
                            // Allows post method on users endpoint to the public
                            .requestMatchers(HttpMethod.POST,"/users").permitAll()
                            // Other endpoints should be authenticated.
                            .anyRequest().authenticated()
            );
        return http.build();
    }
}
