package com.mongodb.Dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Getter
@Setter
@Document
public class DocumentDto {
    @Id
    private String id;
    @Field
    private String fileName;
    @Field
    private String path;
    @Field
    private String deleted = "false";
    @Field
    private Date createdDate;

}
