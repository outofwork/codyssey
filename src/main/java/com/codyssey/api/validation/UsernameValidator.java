package com.codyssey.api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

/**
 * Validator for username following global standards
 */
public class UsernameValidator implements ConstraintValidator<ValidUsername, String> {

    // Username pattern: 3-30 characters, starts with alphanumeric, 
    // contains alphanumeric, underscore, hyphen, dot, no consecutive special chars
    private static final Pattern USERNAME_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9]([a-zA-Z0-9._-]*[a-zA-Z0-9]|[a-zA-Z0-9]*)$"
    );

    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 30;

    @Override
    public void initialize(ValidUsername constraintAnnotation) {
        // Initialization logic if needed
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }

        // Check length
        if (username.length() < MIN_LENGTH || username.length() > MAX_LENGTH) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    String.format("Username must be between %d and %d characters", MIN_LENGTH, MAX_LENGTH)
            ).addConstraintViolation();
            return false;
        }

        // Check pattern
        if (!USERNAME_PATTERN.matcher(username).matches()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Username must start with a letter or number, and can only contain letters, numbers, underscore, hyphen, or dot"
            ).addConstraintViolation();
            return false;
        }

        // Check for consecutive special characters
        if (username.matches(".*[._-]{2,}.*")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Username cannot contain consecutive special characters"
            ).addConstraintViolation();
            return false;
        }

        return true;
    }
}