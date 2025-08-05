package com.codyssey.api.dto.link;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for reordering links
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LinkSequenceDto {
    private String linkId;
    private Integer sequence;
}