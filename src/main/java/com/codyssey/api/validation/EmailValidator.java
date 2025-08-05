package com.codyssey.api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

/**
 * Enhanced email validator following RFC 5322 standards
 */
public class EmailValidator implements ConstraintValidator<ValidEmail, String> {

    // RFC 5322 compliant email pattern (simplified but robust)
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$"
    );

    private static final int MAX_EMAIL_LENGTH = 254; // RFC 5321
    private static final int MAX_LOCAL_PART_LENGTH = 64; // RFC 5321

    @Override
    public void initialize(ValidEmail constraintAnnotation) {
        // Initialization logic if needed
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null) {
            return true; // null values are considered valid (optional field)
        }
        if (email.trim().isEmpty()) {
            return false; // empty string is invalid
        }

        // Trim whitespace
        email = email.trim();

        // Check overall length
        if (email.length() > MAX_EMAIL_LENGTH) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    String.format("Email must not exceed %d characters", MAX_EMAIL_LENGTH)
            ).addConstraintViolation();
            return false;
        }

        // Check basic format
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Email format is invalid"
            ).addConstraintViolation();
            return false;
        }

        // Split and validate parts
        String[] parts = email.split("@");
        if (parts.length != 2) {
            return false;
        }

        String localPart = parts[0];
        String domainPart = parts[1];

        // Check local part length
        if (localPart.length() > MAX_LOCAL_PART_LENGTH) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    String.format("Email local part must not exceed %d characters", MAX_LOCAL_PART_LENGTH)
            ).addConstraintViolation();
            return false;
        }

        // Validate domain has at least one dot and proper format
        if (!domainPart.contains(".") || domainPart.startsWith(".") || domainPart.endsWith(".")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Email domain format is invalid"
            ).addConstraintViolation();
            return false;
        }

        // Check for consecutive dots in domain
        if (domainPart.contains("..")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Email domain cannot contain consecutive dots"
            ).addConstraintViolation();
            return false;
        }

        return true;
    }
}