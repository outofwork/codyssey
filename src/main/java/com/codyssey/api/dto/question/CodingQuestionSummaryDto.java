package com.codyssey.api.dto.question;

import com.codyssey.api.dto.label.LabelSummaryDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object for CodingQuestion summary (for lists/search results)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CodingQuestionSummaryDto {

    private String id;
    
    private String title;
    
    private String shortDescription;
    
    private LabelSummaryDto difficultyLabel;
    
    private String platformSource;
    
    private String status;
    
    private LocalDateTime createdAt;
    
    private Integer solutionsCount;
    
    private List<LabelSummaryDto> primaryTags; // Top 3-5 most important tags
    
    private List<LabelSummaryDto> topCompanies; // Top companies that ask this question
}