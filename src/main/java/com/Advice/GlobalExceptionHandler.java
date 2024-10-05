package com.Advice;


import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


import java.sql.SQLException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BaseApiExcepetions.class )
    public ResponseEntity<ErrorDetails> handleApiExceptions(BaseApiExcepetions ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(ex.getMessage(), request.getDescription(false).substring(4), ex.getStatusCode());
        return new ResponseEntity<>(errorDetails, ex.getStatusCode());

    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorDetails> handleDataIntegrityViolationException(DataIntegrityViolationException ex , WebRequest request) {

        ErrorDetails errorDetails = new ErrorDetails("Email already in use", request.getDescription(false).substring(4), HttpStatus.CONFLICT);
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDetails> handleSQLExceptions(SqlExceptionHelper ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(ex.toString() , request.getDescription(false).substring(4), HttpStatus.CONFLICT);
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);

    }
    @ExceptionHandler(IllegalArgumentException.class )
    public ResponseEntity<ErrorDetails> handleIllegalArgumentExceptions(IllegalArgumentException ex, WebRequest request) {
        ErrorDetails errorDetails = new com.Advice.ErrorDetails(ex.getMessage(), request.getDescription(false).substring(4), HttpStatus.CONFLICT);
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);

    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(ex.getFieldError().getDefaultMessage(), request.getDescription(false).substring(4), (HttpStatus) status);
        return new ResponseEntity<>(errorDetails, status);

    }
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ErrorDetails> handleSQLExceptions(SQLException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(ex.getMessage(), request.getDescription(false).substring(4), HttpStatus.CONFLICT);
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleExceptions(Exception ex, WebRequest request) {
        ErrorDetails errorDetails = new com.Advice.ErrorDetails(ex.getMessage(), request.getDescription(false).substring(4), HttpStatus.CONFLICT);
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);

    }





}