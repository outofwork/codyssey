package com.codyssey.api.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
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
 * <p>
 * Handles all exceptions thrown by controllers and provides
 * consistent error responses across the API.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handle ResourceNotFoundException
     *
     * @param ex      the exception
     * @param request the web request
     * @return ResponseEntity with error details
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {

        log.error("Resource not found: {}", ex.getMessage());

        String userFriendlyMessage = getUserFriendlyNotFoundMessage(ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND, userFriendlyMessage);
        problemDetail.setTitle("Not Found");
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        problemDetail.setProperty("path", request.getDescription(false));
        problemDetail.setProperty("suggestion", getSuggestionForNotFound(ex.getMessage()));

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }

    /**
     * Convert technical not found message to user-friendly message
     */
    private String getUserFriendlyNotFoundMessage(String originalMessage) {
        if (originalMessage.toLowerCase().contains("user") && originalMessage.toLowerCase().contains("id")) {
            return "User not found. Please check the user ID and try again.";
        } else if (originalMessage.toLowerCase().contains("user") && originalMessage.toLowerCase().contains("username")) {
            return "User not found. Please check the username and try again.";
        } else if (originalMessage.toLowerCase().contains("role")) {
            return "Required user role is not configured in the system.";
        }
        return originalMessage;
    }

    /**
     * Provide helpful suggestions for not found resources
     */
    private String getSuggestionForNotFound(String originalMessage) {
        if (originalMessage.toLowerCase().contains("user")) {
            return "Verify the user exists or try searching for users first.";
        } else if (originalMessage.toLowerCase().contains("role")) {
            return "Please contact the system administrator to configure the required roles.";
        }
        return "Please verify the requested resource exists.";
    }

    /**
     * Handle DuplicateResourceException
     *
     * @param ex      the exception
     * @param request the web request
     * @return ResponseEntity with error details
     */
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ProblemDetail> handleDuplicateResourceException(
            DuplicateResourceException ex, WebRequest request) {

        log.error("Duplicate resource: {}", ex.getMessage());

        String userFriendlyMessage = getUserFriendlyDuplicateMessage(ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT, userFriendlyMessage);
        problemDetail.setTitle("Resource Already Exists");
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        problemDetail.setProperty("path", request.getDescription(false));
        problemDetail.setProperty("suggestion", getSuggestionForDuplicate(ex.getMessage()));

        return ResponseEntity.status(HttpStatus.CONFLICT).body(problemDetail);
    }

    /**
     * Convert technical duplicate message to user-friendly message
     */
    private String getUserFriendlyDuplicateMessage(String originalMessage) {
        if (originalMessage.toLowerCase().contains("username")) {
            return "This username is already taken. Please choose a different username.";
        } else if (originalMessage.toLowerCase().contains("email")) {
            return "This email address is already registered. Please use a different email or try logging in.";
        }
        return originalMessage;
    }

    /**
     * Provide helpful suggestions for duplicate resources
     */
    private String getSuggestionForDuplicate(String originalMessage) {
        if (originalMessage.toLowerCase().contains("username")) {
            return "Try adding numbers or underscores to make your username unique.";
        } else if (originalMessage.toLowerCase().contains("email")) {
            return "If you already have an account, try logging in instead of registering.";
        }
        return "Please try with different values.";
    }

    /**
     * Handle validation errors for request body
     *
     * @param ex      the exception
     * @param request the web request
     * @return ResponseEntity with validation error details
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request) {

        log.error("Request body validation error: {}", ex.getMessage());

        Map<String, String> validationErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            validationErrors.put(fieldName, errorMessage);
        });

        String mainMessage = validationErrors.size() == 1 
            ? "Please fix the following issue with your request:"
            : String.format("Please fix the following %d issues with your request:", validationErrors.size());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, mainMessage);
        problemDetail.setTitle("Validation Error");
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        problemDetail.setProperty("path", request.getDescription(false));
        problemDetail.setProperty("errors", validationErrors);
        problemDetail.setProperty("errorCount", validationErrors.size());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    /**
     * Handle validation errors for path variables and request parameters
     *
     * @param ex      the exception
     * @param request the web request
     * @return ResponseEntity with validation error details
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ProblemDetail> handleConstraintViolationException(
            ConstraintViolationException ex, WebRequest request) {

        log.error("Path variable/parameter validation error: {}", ex.getMessage());

        Map<String, String> validationErrors = new HashMap<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String propertyPath = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            // Extract just the parameter name (last part after the dot)
            String parameterName = propertyPath.substring(propertyPath.lastIndexOf('.') + 1);
            validationErrors.put(parameterName, message);
        }

        String mainMessage = validationErrors.size() == 1 
            ? "The provided value is invalid:"
            : String.format("The following %d values are invalid:", validationErrors.size());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, mainMessage);
        problemDetail.setTitle("Invalid Input");
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        problemDetail.setProperty("path", request.getDescription(false));
        problemDetail.setProperty("errors", validationErrors);
        problemDetail.setProperty("errorCount", validationErrors.size());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    /**
     * Handle IllegalArgumentException
     *
     * @param ex      the exception
     * @param request the web request
     * @return ResponseEntity with error details
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ProblemDetail> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) {

        log.error("Illegal argument: {}", ex.getMessage());

        String userFriendlyMessage = getUserFriendlyArgumentMessage(ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, userFriendlyMessage);
        problemDetail.setTitle("Invalid Request");
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        problemDetail.setProperty("path", request.getDescription(false));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    /**
     * Convert technical argument message to user-friendly message
     */
    private String getUserFriendlyArgumentMessage(String originalMessage) {
        if (originalMessage.contains("parameter name information not available")) {
            return "There was an issue processing your request. Please ensure all required information is provided.";
        } else if (originalMessage.toLowerCase().contains("null") || originalMessage.toLowerCase().contains("empty")) {
            return "Required information is missing. Please provide all necessary details.";
        }
        return "Invalid request. Please check your input and try again.";
    }

    /**
     * Handle all other exceptions
     *
     * @param ex      the exception
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