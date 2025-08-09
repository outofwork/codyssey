package com.codyssey.api.dto.mcq;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object for creating MCQ Category association
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MCQCategoryCreateDto {

    private String mcqQuestionId;

    @NotBlank(message = "Category ID is required")
    private String categoryId;

    public MCQCategoryCreateDto(String categoryId) {
        this.categoryId = categoryId;
    }
}
