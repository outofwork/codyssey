package com.codyssey.api.dto.mcq;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object for creating MCQ Label association
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MCQLabelCreateDto {

    @NotBlank(message = "MCQ Question ID is required")
    private String mcqQuestionId;

    @NotBlank(message = "Label ID is required")
    private String labelId;

    @Min(value = 1, message = "Relevance score must be between 1 and 10")
    @Max(value = 10, message = "Relevance score must be between 1 and 10")
    private Integer relevanceScore = 5;

    private Boolean isPrimary = false;

    @Size(max = 500, message = "Notes must not exceed 500 characters")
    private String notes;
}
