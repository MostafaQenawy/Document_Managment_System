package com.mongodb.Mapper;

import com.mongodb.Dto.DirectoryDto;
import com.mongodb.Entity.Directory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DirectoryMapper {
    //map from entity to dto
    DirectoryDto Map(Directory directory);

    //map from dto to entity
    Directory UnMap(DirectoryDto directoryDto);
}
