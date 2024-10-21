package com.mongodb.Advice;

import jakarta.servlet.ServletException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import io.jsonwebtoken.SignatureException;
import java.io.IOException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BaseApiExcepetions.class )
    public ResponseEntity<ErrorDetails> handleApiExceptions(BaseApiExcepetions ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(ex.getMessage(), request.getDescription(false).substring(4), ex.getStatusCode());
        return new ResponseEntity<>(errorDetails, ex.getStatusCode());

    }

    @ExceptionHandler(IOException.class )
    public ResponseEntity<ErrorDetails> handleIOException(IOException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(ex.getMessage(), request.getDescription(false).substring(4), HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ServletException.class )
    public ResponseEntity<ErrorDetails> handleServletException(ServletException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(ex.getMessage(), request.getDescription(false).substring(4), HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(SignatureException.class )
    public ResponseEntity<ErrorDetails> handleSignatureException(SignatureException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(ex.getMessage(), request.getDescription(false).substring(4), HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class )
    public ResponseEntity<ErrorDetails> handleExceptions(Exception ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(ex.getMessage(), request.getDescription(false).substring(4), HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
    }






}