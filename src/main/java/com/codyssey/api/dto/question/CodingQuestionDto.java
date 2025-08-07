package com.codyssey.api.dto.question;

import com.codyssey.api.dto.label.LabelSummaryDto;
import com.codyssey.api.dto.source.SourceSummaryDto;
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
    
    private String filePath;
    
    private String contentUrl;
    
    private LabelSummaryDto difficultyLabel;
    
    private SourceSummaryDto source;
    
    private String platformQuestionId;
    
    private String originalUrl;
    
    private String status;
    
    private UserDto createdByUser;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    private Long version;
    
    // Related entities counts/summaries
    private List<LabelSummaryDto> tags;
    
    private List<LabelSummaryDto> companies;
}