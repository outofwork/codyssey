package com.codyssey.api.dto.solution;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for updating a QuestionSolution
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionSolutionUpdateDto {

    private Integer sequence;

    @Size(max = 50, message = "Language must not exceed 50 characters")
    private String language;

    private String solutionCode;

    private String explanation;

    @Size(max = 50, message = "Time complexity must not exceed 50 characters")
    private String timeComplexity;

    @Size(max = 50, message = "Space complexity must not exceed 50 characters")
    private String spaceComplexity;

    private Boolean isOptimal;
}