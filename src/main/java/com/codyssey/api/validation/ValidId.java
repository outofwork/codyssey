package com.codyssey.api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Custom validation annotation for 15-character alphanumeric IDs
 * Validates that the ID is exactly 15 characters and contains only
 * letters (both uppercase and lowercase) and digits.
 */
@Documented
@Constraint(validatedBy = IdValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidId {

    String message() default "ID must be exactly 15 characters and contain only letters and numbers";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}