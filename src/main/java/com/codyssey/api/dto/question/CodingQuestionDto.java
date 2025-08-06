package com.codyssey.api.dto.question;

import com.codyssey.api.dto.label.LabelSummaryDto;
import com.codyssey.api.dto.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object for CodingQuestion with full details
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CodingQuestionDto {

    private String id;
    
    private String title;
    
    private String shortDescription;
    
    private String description;
    
    private LabelSummaryDto difficultyLabel;
    
    private String platformSource;
    
    private String platformQuestionId;
    
    private String inputFormat;
    
    private String outputFormat;
    
    private String constraintsText;
    
    private String timeComplexityHint;
    
    private String spaceComplexityHint;
    
    private String status;
    
    private UserDto createdByUser;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    private Long version;
    
    // Related entities counts/summaries
    private List<LabelSummaryDto> tags;
    
    private List<LabelSummaryDto> companies;
}