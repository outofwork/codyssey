package com.codyssey.api.dto.solution;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for creating a new QuestionSolution
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionSolutionCreateDto {

    @NotBlank(message = "Question ID is required")
    private String questionId;

    @NotNull(message = "Sequence is required")
    private Integer sequence;

    @NotBlank(message = "Language is required")
    @Size(max = 50, message = "Language must not exceed 50 characters")
    private String language;

    @NotBlank(message = "Solution code is required")
    private String solutionCode;

    private String explanation;

    @Size(max = 50, message = "Time complexity must not exceed 50 characters")
    private String timeComplexity;

    @Size(max = 50, message = "Space complexity must not exceed 50 characters")
    private String spaceComplexity;

    private Boolean isOptimal = false;

    private String createdByUserId;
}