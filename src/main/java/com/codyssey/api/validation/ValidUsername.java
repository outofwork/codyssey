package com.codyssey.api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Custom validation annotation for username
 * Validates username against global standards:
 * - Length: 3-30 characters
 * - Alphanumeric characters, underscore, hyphen, and dot allowed
 * - Cannot start or end with special characters
 * - No consecutive special characters
 * - Must start with alphanumeric character
 */
@Documented
@Constraint(validatedBy = UsernameValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUsername {

    String message() default "Username must be 3-30 characters, start with a letter or number, and contain only letters, numbers, underscore (_), hyphen (-), or dot (.)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}