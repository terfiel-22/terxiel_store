package com.terxiel.store.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * DTO for {@link com.terxiel.store.entities.User}
 */
public record LoginRequest(
        @NotNull(message = "Email is required.")
        @Email(message = "Email is not valid.")
        String email,

        @NotNull(message = "Password is required.")
        String password
) implements Serializable {
}