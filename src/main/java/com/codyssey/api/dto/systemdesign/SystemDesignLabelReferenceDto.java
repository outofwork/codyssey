package com.codyssey.api.dto.systemdesign;

import com.codyssey.api.dto.label.LabelSummaryDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for SystemDesignLabel reference data
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemDesignLabelReferenceDto {

    private String id;
    
    private String systemDesignId;
    
    private LabelSummaryDto label;
    
    private Boolean isPrimary;
    
    private Integer displayOrder;
    
    private LocalDateTime createdAt;
}
