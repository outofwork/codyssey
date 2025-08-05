package com.codyssey.api.validation;

import com.codyssey.api.util.CategoryIdGenerator;
import com.codyssey.api.util.LabelIdGenerator;
import com.codyssey.api.util.RoleIdGenerator;
import com.codyssey.api.util.UserIdGenerator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator for prefixed IDs
 * <p>
 * Validates that an ID is in the correct format:
 * - User IDs: ACC-xxxxxx
 * - Category IDs: CAT-xxxxxx
 * - Role IDs: ROLE-xxxxxx
 * - Label IDs: LBL-xxxxxx
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

        // Check if it's a valid User ID, Category ID, Role ID, or Label ID
        return UserIdGenerator.isValidUserId(id) ||
                CategoryIdGenerator.isValidCategoryId(id) ||
                RoleIdGenerator.isValidRoleId(id) ||
                LabelIdGenerator.isValidLabelId(id);
    }
}