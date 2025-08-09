package com.codyssey.api.dto.navigation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object for Navigation Links
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NavigationLinksDto {

    private String mcqsUrl;
    private String randomMcqsUrl;
    private String labelsUrl;
    private String parentUrl;
    private String childrenUrl;
}
