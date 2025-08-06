package com.codyssey.api.dto.question;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for bulk creating question-label relationships
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionLabelBulkCreateDto {

    @NotBlank(message = "Question ID is required")
    private String questionId;

    @NotEmpty(message = "At least one label assignment is required")
    @Valid
    private List<LabelAssignment> labelAssignments;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LabelAssignment {
        @NotBlank(message = "Label ID is required")
        private String labelId;
        
        private Integer relevanceScore = 5;
        private Boolean isPrimary = false;
        private String notes;
    }
}