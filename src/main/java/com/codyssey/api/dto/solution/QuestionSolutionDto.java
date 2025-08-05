package com.codyssey.api.dto.solution;

import com.codyssey.api.dto.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for QuestionSolution with full details
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionSolutionDto {

    private String id;
    
    private String questionId;
    
    private Integer sequence;
    
    private String language;
    
    private String solutionCode;
    
    private String explanation;
    
    private String timeComplexity;
    
    private String spaceComplexity;
    
    private Boolean isOptimal;
    
    private UserDto createdByUser;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    private Long version;
}