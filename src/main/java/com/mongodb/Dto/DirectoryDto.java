package com.mongodb.Dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
@Getter
@Setter
@Document
public class DirectoryDto {
    @Id
    private String id;
    @Field
    private String name;
    @Field
    private String path;
    @Field
    private String deleted ;
    @Field
    @JsonFormat(shape = JsonFormat.Shape.STRING  , pattern = "yyyy-mm-dd 'T' HH:mm")
    private Date createdDate;
}
