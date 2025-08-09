package com.codyssey.api.dto.navigation;

import com.codyssey.api.dto.label.LabelSummaryDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Data Transfer Object for Label Navigation
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LabelNavigationDto {

    private String id;
    private String name;
    private String description;
    private String urlSlug;
    private String categoryId;
    private String categoryName;
    private LabelSummaryDto parent;
    private List<LabelSummaryDto> children;
    private Boolean hasChildren;
    private Boolean isRoot;
    private Long mcqCount;
    private Long totalMcqCountInHierarchy;
    private NavigationLinksDto navigationLinks;
}
