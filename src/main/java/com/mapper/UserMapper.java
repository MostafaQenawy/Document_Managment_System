package com.mapper;

import com.DTO.UserDto;
import com.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")

public interface UserMapper {
    //map from entity to Dto
    @Mapping(target = "password" , ignore = true)
    UserDto Map(User user);

    //map from dto to entity
    User UnMap(UserDto userDto);
}
