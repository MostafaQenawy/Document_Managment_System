package com.mongodb.Mapper;

import com.mongodb.Dto.WorkSpaceDto;
import com.mongodb.Entity.WorkSpace;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface WorkSpaceMapper {

    WorkSpaceDto Map(WorkSpace workSpace);

    //map from dto to entity
    WorkSpace UnMap(WorkSpaceDto workSpaceDto);

}