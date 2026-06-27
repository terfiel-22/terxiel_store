package com.terxiel.store.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Projection for {@link com.terxiel.store.entities.User}
 */
public record ChangePasswordRequest(
        String oldPassword,
        String newPassword
) {
}