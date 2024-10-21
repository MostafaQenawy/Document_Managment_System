package com.mongodb.Dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Getter
@Setter
@Document
public class MetadataDto {
    @Id
    private String id;
    @Field
    private String contentType;
    @Field
    private Long versioning_info ;
    @Field
    private List<String> tags;
    @Field
    private String size;
    @Field
    private String access_controls ;

}
