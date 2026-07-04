package com.terxiel.store.dtos;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.terxiel.store.entities.Role;

import java.time.LocalDateTime;

/**
 * Projection for {@link com.terxiel.store.entities.User}
 */
public record UserSummary(
        @JsonIgnore
        Long id,

        String email,

        @JsonProperty("full_name")
        String name,

        Role role,

        @JsonFormat(pattern = "MMMM d, yyyy, h:mm a")
        LocalDateTime createdAt
) {
}
