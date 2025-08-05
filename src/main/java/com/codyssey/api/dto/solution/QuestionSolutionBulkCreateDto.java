package com.codyssey.api.dto.solution;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Data Transfer Object for bulk creating QuestionSolutions
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionSolutionBulkCreateDto {

    @NotEmpty(message = "Solutions list cannot be empty")
    @Valid
    private List<QuestionSolutionCreateDto> solutions;
}