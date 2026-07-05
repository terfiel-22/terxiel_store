package com.terxiel.store.controllers;

import com.terxiel.store.exceptions.AuthenticationNotFoundException;
import com.terxiel.store.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> handleValidationErrors(MethodArgumentNotValidException exception)
    {
        var errors = new HashMap<String,String>();
        exception.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(),error.getDefaultMessage()));

        return ResponseEntity.unprocessableContent().body(errors);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String,String>> handleUserNotFound()
    {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of("error","User not found.")
        );
    }

    @ExceptionHandler(AuthenticationNotFoundException.class)
    public ResponseEntity<Map<String,String>> handleAuthenticationNotFound()
    {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                Map.of("error","You are not currently signed in.")
        );
    }
}
