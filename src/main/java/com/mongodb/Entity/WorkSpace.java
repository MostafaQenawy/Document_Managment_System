package com.mongodb.Entity;

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
public class WorkSpace {
    @Id
    private String id;
    @Field
    private Long userId;

    @Field
    private String name;

    @Field
    private String path;
    @Field
    private String deleted = "false";

    @Field
    @JsonFormat(shape = JsonFormat.Shape.STRING  , pattern = "yyyy-mm-dd 'T' HH:mm")
    private Date createdDate;

    public WorkSpace(Long userId, String name , String path) {
        this.userId = userId;
        this.name = name;
        this.path = path;
        this.createdDate = new Date();
    }


}
