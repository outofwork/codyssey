package com.codyssey.api.dto.question;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO for creating a question-company relationship
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionCompanyCreateDto {

    @NotBlank(message = "Question ID is required")
    private String questionId;

    @NotBlank(message = "Company label ID is required")
    private String companyLabelId;

    @Min(value = 1, message = "Frequency score must be between 1 and 10")
    @Max(value = 10, message = "Frequency score must be between 1 and 10")
    private Integer frequencyScore = 5;

    private String interviewRound; // ONLINE_ASSESSMENT, PHONE_SCREEN, etc.

    private Integer lastAskedYear;

    private LocalDate lastAskedDate;

    @Size(max = 500, message = "Notes must not exceed 500 characters")
    private String notes;

    private Boolean isVerified = false;
}