package com.codyssey.api.dto.link;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for QuestionLink
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionLinkDto {
    private String id;
    private String sourceQuestionId;
    private String sourceQuestionTitle;
    private String targetQuestionId;
    private String targetQuestionTitle;
    private Integer sequence;
    private String relationshipType;
}