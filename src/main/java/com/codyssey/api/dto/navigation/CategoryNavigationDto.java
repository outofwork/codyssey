package com.codyssey.api.dto.navigation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object for Category Navigation
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryNavigationDto {

    private String id;
    private String name;
    private String code;
    private String description;
    private String urlSlug;
    private long labelCount;
}
