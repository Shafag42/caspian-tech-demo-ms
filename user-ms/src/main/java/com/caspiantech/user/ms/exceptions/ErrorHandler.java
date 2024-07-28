package com.caspiantech.user.ms.exceptions;

import lombok.extern.java.Log;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

import static org.springframework.http.HttpStatus.*;

@Log
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(UNAUTHORIZED)
    public ErrorResponse handle(UnauthorizedException ex) {
        log.throwing("UnauthorizedException","Unauthorized access attempt: {}", ex);
        return ErrorResponse.builder()
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse handle(UserNotFoundException ex) {
        log.throwing("UserNotFoundException", "User not found: {}", ex);
        return ErrorResponse.builder()
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    @ResponseStatus(CONFLICT)
    public ErrorResponse handle(EmailAlreadyExistsException ex) {
        log.throwing("EmailAlreadyExistsException", "Email already exists: {}", ex);
        return ErrorResponse.builder()
                .message(ex.getMessage())
                .build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorResponse handle(Exception ex) {
        log.throwing("Exception", "An unexpected error occurred: {}", ex);
        return new ErrorResponse("An unexpected error occurred: " + ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handle(MethodArgumentNotValidException ex) {
        log.throwing("MethodArgumentNotValidException", "Validation failed: {}", ex);
        return ErrorResponse.builder()
                .message(ex.getBindingResult().getFieldError().getDefaultMessage())
                .build();
    }

    @ExceptionHandler(IOException.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorResponse handle(IOException ex) {
        log.throwing("IOException", "Failed to process image: {}", ex);
        return new ErrorResponse("Failed to process image: " + ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handle(IllegalArgumentException ex) {
        log.throwing("IllegalArgumentException", "Invalid argument: {}", ex);
        return ErrorResponse.builder()
                .message(ex.getMessage())
                .build();
    }
}