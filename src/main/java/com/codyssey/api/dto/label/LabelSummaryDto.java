package com.codyssey.api.dto.label;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Simplified Data Transfer Object for Label references in Label responses
 * <p>
 * Contains only essential label information (id and name) to keep Label API responses clean.
 * Used for parent/child references within Label responses.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabelSummaryDto {

    private String id;
    private String name;
}