package com.codyssey.api.exception;

/**
 * Exception thrown when a requested resource is not found
 * <p>
 * This exception is typically thrown when attempting to retrieve
 * an entity by ID or other unique identifier that doesn't exist.
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructs a new ResourceNotFoundException with the specified detail message
     *
     * @param message the detail message
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new ResourceNotFoundException with the specified detail message and cause
     *
     * @param message the detail message
     * @param cause   the cause
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}