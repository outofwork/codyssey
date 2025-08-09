package com.codyssey.api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Custom validation annotation for email
 * Validates email against RFC 5322 standards:
 * - Maximum length: 254 characters (RFC 5321)
 * - Proper email format validation
 * - Local part max 64 characters
 * - Domain part validation
 */
@Documented
@Constraint(validatedBy = EmailValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEmail {

    String message() default "Please enter a valid email address (e.g., user@example.com)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}