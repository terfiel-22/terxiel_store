package com.terxiel.store.controllers;

import com.terxiel.store.shared.dtos.ErrorDTO;
import com.terxiel.store.modules.auth.exceptions.AuthenticationNotFoundException;
import com.terxiel.store.modules.user.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
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
    public ResponseEntity<ErrorDTO> handleUserNotFound()
    {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorDTO("User not found.")
        );
    }

    @ExceptionHandler(AuthenticationNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleAuthenticationNotFound()
    {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                new ErrorDTO("You are not currently signed in.")
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDTO> handleUnreadableMessage()
    {
        return ResponseEntity.badRequest().body(new ErrorDTO("Invalid request body"));
    }
}
