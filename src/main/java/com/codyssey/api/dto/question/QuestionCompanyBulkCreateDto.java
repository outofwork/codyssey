package com.codyssey.api.dto.question;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Data Transfer Object for bulk creating QuestionCompany associations
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionCompanyBulkCreateDto {

    @NotEmpty(message = "Question companies list cannot be empty")
    @Valid
    private List<QuestionCompanyCreateDto> questionCompanies;
}