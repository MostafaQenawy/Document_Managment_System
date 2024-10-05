package com.Advice;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.util.Date;

public class ErrorDetails {
    private String message;
    @JsonFormat(shape = JsonFormat.Shape.STRING , pattern = "dd-mm-yyyy hh:mm:ss")
    private Date timestamp;
    private String uri ;
    private HttpStatus StatusCode;
    public ErrorDetails(){
        timestamp = new Date();
    }
    public ErrorDetails(String message, String uri, HttpStatus statusCode) {
       this();
        this.message = message;
        this.uri = uri;
        StatusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getUri() {
        return uri;
    }

    public void setUrl(String uri) {
        this.uri = uri;
    }

    public HttpStatus getStatusCode() {
        return StatusCode;
    }

    public void setStatusCode(HttpStatus statusCode) {
        StatusCode = statusCode;
    }
}
