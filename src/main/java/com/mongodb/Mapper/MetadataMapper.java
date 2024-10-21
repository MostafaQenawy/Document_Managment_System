package com.mongodb.Mapper;

import com.mongodb.Dto.MetadataDto;
import com.mongodb.Entity.Metadata;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MetadataMapper {

    //map from entity to dto
    MetadataDto Map(Metadata metadata);

    //map from dto to entity
    Metadata UnMap(MetadataDto metadataDto);
}
