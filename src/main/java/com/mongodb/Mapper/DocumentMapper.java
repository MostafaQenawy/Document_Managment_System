package com.mongodb.Mapper;

import com.mongodb.Dto.DirectoryDto;
import com.mongodb.Dto.DocumentDto;
import com.mongodb.Entity.Directory;
import com.mongodb.Entity.Documents;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DocumentMapper {
    //map from entity to dto
    DocumentDto Map(Documents document);

    //map from dto to entity
    Documents UnMap(DocumentDto documentDto);
}
