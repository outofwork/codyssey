package com.codyssey.api.dto.question;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for label usage statistics
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabelUsageDto {
    private String labelId;
    private String labelName;
    private String categoryCode;
    private Long usageCount;
}