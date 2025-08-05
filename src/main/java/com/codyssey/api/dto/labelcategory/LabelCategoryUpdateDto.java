package com.codyssey.api.dto.labelcategory;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for updating LabelCategory
 * <p>
 * Used for transferring label category update data.
 * Code cannot be updated after creation.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabelCategoryUpdateDto {

    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    private Boolean active;
}