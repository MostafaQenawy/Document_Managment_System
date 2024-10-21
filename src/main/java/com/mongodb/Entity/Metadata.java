package com.mongodb.Entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Document
public class Metadata {
    @Id
    private String id;
    @Field
    private String contentType;
    @Field
    private Long versioning_info = 1L;
    @Field
    private List<String> tags ;
    @Field
    private Long size;
    @Field
    private String access_controls = "user";

    public Metadata(String id, String contentType , Long size) {
        this.id = id;
        this.contentType = contentType;
        this.tags = new ArrayList<>();
        this.size = size;
    }
}
