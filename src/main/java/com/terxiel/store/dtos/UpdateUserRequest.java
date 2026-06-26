package com.terxiel.store.dtos;

/**
 * Projection for {@link com.terxiel.store.entities.User}
 */
public record UpdateUserRequest(String name, String email) {
}