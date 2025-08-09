package com.codyssey.api.dto.systemdesign;

import com.codyssey.api.dto.label.LabelSummaryDto;
import com.codyssey.api.dto.source.SourceSummaryDto;
import com.codyssey.api.dto.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object for SystemDesign with full details
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemDesignDto {

    private String id;
    
    private String title;
    
    private String shortDescription;
    
    private SourceSummaryDto source;
    
    private String originalUrl;
    
    private String status;
    
    private String uri;
    
    private String contentUrl;
    
    private UserDto createdByUser;
    
    private Long version;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    // Related entities counts/summaries
    private List<LabelSummaryDto> tags;
}
