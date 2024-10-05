package com.mapper;

import com.DTO.RoleDto;
import com.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    //map from entity to Dto
    RoleDto Map(Role role);

    //map from dto to entity
    Role UnMap(RoleDto roleDto);
}
