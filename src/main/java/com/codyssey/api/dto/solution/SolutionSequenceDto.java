package com.codyssey.api.dto.solution;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for reordering solutions
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolutionSequenceDto {
    private String solutionId;
    private Integer sequence;
}