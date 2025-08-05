package com.codyssey.api.dto.testcase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for reordering test cases
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestCaseSequenceDto {
    private String testCaseId;
    private Integer sequence;
}