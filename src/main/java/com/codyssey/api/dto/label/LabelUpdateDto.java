package com.codyssey.api.dto.label;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for updating a Label
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabelUpdateDto {

    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    private String parentId;

    private Boolean active;
}