package com.codyssey.api.dto.labelcategory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for creating new LabelCategory
 * <p>
 * Used for transferring label category creation data.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabelCategoryCreateDto {

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @NotBlank(message = "Code is required")
    @Size(max = 50, message = "Code must not exceed 50 characters")
    @Pattern(
            regexp = "^[A-Z0-9_]+$",
            message = "Code must contain only uppercase letters, numbers, and underscores"
    )
    private String code;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    private Boolean active = true;
}