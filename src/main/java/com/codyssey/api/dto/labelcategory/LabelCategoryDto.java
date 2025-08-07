package com.codyssey.api.dto.labelcategory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for LabelCategory entity
 * <p>
 * Used for transferring label category data between layers.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabelCategoryDto {

    private String id;

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @NotBlank(message = "Code is required")
    @Size(max = 50, message = "Code must not exceed 50 characters")
    private String code;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    private Boolean active;

    private String uri;
}