package com.real.interview.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for the application.
 * Catches custom exceptions and maps them to appropriate HTTP status codes.
 *
 * @ControllerAdvice is the recommended and most common approach for centralized exception handling in Spring REST APIs
 */
@ControllerAdvice
public class GlobalExceptionHandler {


    /**
     * Handles MovieNotFoundException, returning HTTP 404 Not Found.
     */
    @ExceptionHandler(MovieNotFoundException.class)
    public ResponseEntity<Map<String,Object>> handleMovieNotFoundException(MovieNotFoundException movieNotFoundException)
    {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("status", HttpStatus.NOT_FOUND.value());
        errorDetails.put("error", "Not Found");
        errorDetails.put("message", movieNotFoundException.getMessage());
        errorDetails.put("timestamp", new Date());

        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }
}
