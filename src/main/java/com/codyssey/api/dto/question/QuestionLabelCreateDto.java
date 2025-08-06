package com.codyssey.api.dto.question;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating a question-label relationship
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionLabelCreateDto {

    @NotBlank(message = "Question ID is required")
    private String questionId;

    @NotBlank(message = "Label ID is required")
    private String labelId;

    @Min(value = 1, message = "Relevance score must be between 1 and 10")
    @Max(value = 10, message = "Relevance score must be between 1 and 10")
    private Integer relevanceScore = 5;

    private Boolean isPrimary = false;

    @Size(max = 500, message = "Notes must not exceed 500 characters")
    private String notes;
}