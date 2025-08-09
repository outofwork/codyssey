package com.codyssey.api.dto.mcq;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object for creating MCQ Label association
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MCQLabelCreateDto {

    private String mcqQuestionId;

    @NotBlank(message = "Label ID is required")
    private String labelId;

    public MCQLabelCreateDto(String labelId) {
        this.labelId = labelId;
    }
}
