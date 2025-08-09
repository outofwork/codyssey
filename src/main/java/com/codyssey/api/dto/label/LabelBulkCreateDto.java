package com.codyssey.api.dto.label;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Data Transfer Object for bulk creating Labels with parent-child relationships
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabelBulkCreateDto {

    @NotEmpty(message = "Labels list cannot be empty")
    @Valid
    private List<LabelCreateDto> labels;
}