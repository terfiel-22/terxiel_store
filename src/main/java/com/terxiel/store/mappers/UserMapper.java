package com.terxiel.store.mappers;

import com.terxiel.store.modules.user.dtos.RegisterUserRequest;
import com.terxiel.store.modules.user.dtos.UpdateUserRequest;
import com.terxiel.store.modules.user.dtos.UserSummary;
import com.terxiel.store.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    UserSummary toDto(User user);

    User toEntity(RegisterUserRequest registerUserRequest);

    void update(UpdateUserRequest updateUserRequest, @MappingTarget User user);
}
