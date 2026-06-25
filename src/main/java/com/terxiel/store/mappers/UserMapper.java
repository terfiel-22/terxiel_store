package com.terxiel.store.mappers;

import com.terxiel.store.dtos.UserSummary;
import com.terxiel.store.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserSummary toDto(User user);
}
