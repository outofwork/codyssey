package com.codyssey.api.dto.testcase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for updating test cases
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionTestCaseUpdateDto {
    private Integer sequence;
    private String inputData;
    private String expectedOutput;
    private Boolean isSample;
    private String explanation;
}