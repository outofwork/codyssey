package com.codyssey.api.dto.testcase;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Data Transfer Object for bulk creating QuestionTestCases
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionTestCaseBulkCreateDto {

    @NotEmpty(message = "Test cases list cannot be empty")
    @Valid
    private List<QuestionTestCaseCreateDto> testCases;
}