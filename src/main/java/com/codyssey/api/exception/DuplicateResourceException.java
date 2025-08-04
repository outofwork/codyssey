package com.codyssey.api.exception;

/**
 * Exception thrown when attempting to create a resource that already exists
 * 
 * This exception is typically thrown when attempting to create
 * an entity with a unique field that already exists in the database.
 */
public class DuplicateResourceException extends RuntimeException {

    /**
     * Constructs a new DuplicateResourceException with the specified detail message
     * 
     * @param message the detail message
     */
    public DuplicateResourceException(String message) {
        super(message);
    }

    /**
     * Constructs a new DuplicateResourceException with the specified detail message and cause
     * 
     * @param message the detail message
     * @param cause the cause
     */
    public DuplicateResourceException(String message, Throwable cause) {
        super(message, cause);
    }
}