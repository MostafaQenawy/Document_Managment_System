package com.mongodb.Advice;

import org.springframework.http.HttpStatus;

public class BaseApiExcepetions extends RuntimeException {
    private HttpStatus httpStatus;

    public BaseApiExcepetions(String message , HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus ;
    }
    public HttpStatus getStatusCode(){
        return httpStatus;
    }

}
