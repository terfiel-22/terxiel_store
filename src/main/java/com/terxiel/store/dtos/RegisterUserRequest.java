package com.terxiel.store.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Projection for {@link com.terxiel.store.entities.User}
 */
public record RegisterUserRequest(

        @NotBlank(message = "Name is required.")
        @Size(max = 255, message = "Name must be less than 255 characters")
        String name,

        @NotBlank(message = "Email is required.")
        @Email(message = "Email must be valid.")
        String email,

        @NotBlank(message = "Password is required.")
        @Size(min = 6, max = 25, message = "Password must be between 6 - 25 characters long.")
        String password
) {
}