package com.codyssey.api.dto.media;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for reordering media files
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MediaSequenceDto {
    private String mediaId;
    private Integer sequence;
}