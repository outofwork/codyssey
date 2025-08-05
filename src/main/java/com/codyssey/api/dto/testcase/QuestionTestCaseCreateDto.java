package com.codyssey.api.dto.testcase;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for creating a QuestionTestCase
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionTestCaseCreateDto {

    @NotBlank(message = "Question ID is required")
    private String questionId;

    @NotNull(message = "Sequence is required")
    private Integer sequence;

    @NotBlank(message = "Input data is required")
    private String inputData;

    @NotBlank(message = "Expected output is required")
    private String expectedOutput;

    private Boolean isSample = false;

    private String explanation;
}