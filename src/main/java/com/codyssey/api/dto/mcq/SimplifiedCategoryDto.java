package com.codyssey.api.dto.mcq;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Simplified Data Transfer Object for Category in MCQ responses
 * Contains only name and uri for clean API responses
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SimplifiedCategoryDto {

    private String name;
    private String uri;
}
