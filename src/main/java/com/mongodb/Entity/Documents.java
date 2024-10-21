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
@Document(collection = "Document")
public class Documents {
    @Id
    private String id;
    @Field
    private String directoryId;
    @Field
    private String fileName;
    @Field
    private String path;
    @Field
    private String deleted = "false";

    @Field
    @JsonFormat(shape = JsonFormat.Shape.STRING  , pattern = "yyyy-mm-dd 'T' HH:mm")
    private Date createdDate;
    public Documents(String directoryId , String fileName , String path){
        this.directoryId =directoryId;
        this.fileName = fileName;
        createdDate = new Date();
        this.path= path;
    }

}
