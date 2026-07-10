package com.terxiel.store.modules.user.dtos;

/**
 * Projection for {@link com.terxiel.store.entities.User}
 */
public record ChangePasswordRequest(
        String oldPassword,
        String newPassword
) {
}