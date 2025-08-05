package com.codyssey.api.dto.testcase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for QuestionTestCase with full details
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionTestCaseDto {

    private String id;
    
    private String questionId;
    
    private Integer sequence;
    
    private String inputData;
    
    private String expectedOutput;
    
    private Boolean isSample;
    
    private String explanation;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    private Long version;
}