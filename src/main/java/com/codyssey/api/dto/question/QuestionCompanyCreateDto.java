package com.codyssey.api.dto.question;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for creating a QuestionCompany association
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionCompanyCreateDto {

    @NotBlank(message = "Question ID is required")
    private String questionId;

    @NotBlank(message = "Company label ID is required")
    private String companyLabelId;

    private Integer frequency = 1;

    private Integer lastAskedYear;
}