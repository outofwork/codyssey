package com.codyssey.api.dto.link;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for creating a QuestionLink
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionLinkCreateDto {

    @NotBlank(message = "Source question ID is required")
    private String sourceQuestionId;

    @NotBlank(message = "Target question ID is required")
    private String targetQuestionId;

    @NotNull(message = "Sequence is required")
    private Integer sequence;

    @NotBlank(message = "Relationship type is required")
    private String relationshipType; // FOLLOW_UP, SIMILAR, PREREQUISITE
}