package com.codyssey.api.validation;

import com.codyssey.api.util.AlphanumericIdGenerator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator for 15-character alphanumeric IDs
 */
public class IdValidator implements ConstraintValidator<ValidId, String> {

    @Override
    public void initialize(ValidId constraintAnnotation) {
        // Initialization logic if needed
    }

    @Override
    public boolean isValid(String id, ConstraintValidatorContext context) {
        if (id == null) {
            return false;
        }
        
        return AlphanumericIdGenerator.isValidId(id);
    }
}