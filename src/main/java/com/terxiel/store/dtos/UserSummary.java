package com.terxiel.store.dtos;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

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

        @JsonFormat(pattern = "MMMM d, yyyy, h:mm a")
        LocalDateTime createdAt
) {
}
