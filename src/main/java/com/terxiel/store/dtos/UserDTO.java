package com.terxiel.store.dtos;

/**
 * Projection for {@link com.terxiel.store.entities.User}
 */
public record UserDTO(
        Long id,
        String name,
        String email,
        String password
) {
}