package com.codyssey.api.dto.systemdesign;

import com.codyssey.api.dto.label.LabelSummaryDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object for SystemDesign summary (for lists/search results)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemDesignSummaryDto {

    private String id;
    
    private String title;
    
    private String shortDescription;
    
    private String sourceName;
    
    private String status;
    
    private String uri;
    
    private String contentUrl;
    
    private LocalDateTime createdAt;
    
    private List<LabelSummaryDto> primaryTags; // Top 3-5 most important tags
}
