package com.terxiel.store.mappers;

import com.terxiel.store.dtos.RegisterUserRequest;
import com.terxiel.store.dtos.UserSummary;
import com.terxiel.store.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    UserSummary toDto(User user);

    User toEntity(RegisterUserRequest registerUserRequest);
}
