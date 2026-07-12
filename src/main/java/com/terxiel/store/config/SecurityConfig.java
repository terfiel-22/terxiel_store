package com.terxiel.store.config;

import com.terxiel.store.filters.JwtAuthenticationFilter;
import com.terxiel.store.modules.auth.handlers.JwtLogoutHandler;
import com.terxiel.store.shared.security.SecurityRules;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@AllArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtLogoutHandler jwtLogoutHandler;
    private List<SecurityRules> featureSecurityRules;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
    {
        http
            // Stateless session (token-based authentication)
            .sessionManagement(c->c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // Disable CSRF
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(
                    matcher->{
                        featureSecurityRules.forEach(securityRules -> securityRules.configure(matcher));
                        matcher.anyRequest().authenticated();
                    }
            )

                // The Modern Approach (Stateless JWT Blacklisting)
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")                // POST request triggers server logout
                        .invalidateHttpSession(true)             // Server-side: Destroys HTTP session
                        .clearAuthentication(true)               // Server-side: Clears SecurityContext
                        .deleteCookies("refreshToken") // Client-side: Orders browser to delete cookies
                        .addLogoutHandler(jwtLogoutHandler)
                        .logoutSuccessHandler((req,res,auth)->{
                            res.setStatus(HttpStatus.OK.value());
                        })
                )

                // Inject JWT Filter
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                .exceptionHandling(ex->{
                    // If the client tries to access a protected endpoint, by default, Spring should return an Unauthorized error.
                    ex.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));

                    // If the access to the endpoint is denied, returns a 403 Forbidden status.
                    ex.accessDeniedHandler(
                            ((request, response, accessDeniedException)
                                    -> response.setStatus(HttpStatus.FORBIDDEN.value())
                            ));
                });

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider()
    {
        var provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
    {
        return config.getAuthenticationManager();
    }
}
