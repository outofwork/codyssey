package com.codyssey.api.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Global Exception Handler for the application
 * 
 * Handles all exceptions thrown by controllers and provides
 * consistent error responses across the API.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handle ResourceNotFoundException
     * 
     * @param ex the exception
     * @param request the web request
     * @return ResponseEntity with error details
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {
        
        log.error("Resource not found: {}", ex.getMessage());
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle("Resource Not Found");
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        problemDetail.setProperty("path", request.getDescription(false));
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }

    /**
     * Handle DuplicateResourceException
     * 
     * @param ex the exception
     * @param request the web request
     * @return ResponseEntity with error details
     */
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ProblemDetail> handleDuplicateResourceException(
            DuplicateResourceException ex, WebRequest request) {
        
        log.error("Duplicate resource: {}", ex.getMessage());
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT, ex.getMessage());
        problemDetail.setTitle("Duplicate Resource");
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        problemDetail.setProperty("path", request.getDescription(false));
        
        return ResponseEntity.status(HttpStatus.CONFLICT).body(problemDetail);
    }

    /**
     * Handle validation errors
     * 
     * @param ex the exception
     * @param request the web request
     * @return ResponseEntity with validation error details
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request) {
        
        log.error("Validation error: {}", ex.getMessage());
        
        Map<String, String> validationErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            validationErrors.put(fieldName, errorMessage);
        });
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, "Validation failed");
        problemDetail.setTitle("Validation Error");
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        problemDetail.setProperty("path", request.getDescription(false));
        problemDetail.setProperty("validationErrors", validationErrors);
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    /**
     * Handle IllegalArgumentException
     * 
     * @param ex the exception
     * @param request the web request
     * @return ResponseEntity with error details
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ProblemDetail> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) {
        
        log.error("Illegal argument: {}", ex.getMessage());
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setTitle("Invalid Argument");
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        problemDetail.setProperty("path", request.getDescription(false));
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    /**
     * Handle all other exceptions
     * 
     * @param ex the exception
     * @param request the web request
     * @return ResponseEntity with generic error details
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGenericException(
            Exception ex, WebRequest request) {
        
        log.error("Unexpected error occurred: ", ex);
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
        problemDetail.setTitle("Internal Server Error");
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        problemDetail.setProperty("path", request.getDescription(false));
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problemDetail);
    }
}