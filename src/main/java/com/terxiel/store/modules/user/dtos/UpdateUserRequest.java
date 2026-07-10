package com.terxiel.store.modules.user.dtos;

import com.terxiel.store.entities.Role;

/**
 * Projection for {@link com.terxiel.store.entities.User}
 */
public record UpdateUserRequest(String name, String email, Role role) {
}