package com.terxiel.store.dtos;

/**
 * Projection for {@link com.terxiel.store.entities.User}
 */
public record RegisterUserRequest(
        String name,
        String email,
        String password
) {
}