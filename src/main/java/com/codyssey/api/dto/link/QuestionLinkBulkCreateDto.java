package com.codyssey.api.dto.link;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Data Transfer Object for bulk creating QuestionLinks
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionLinkBulkCreateDto {

    @NotEmpty(message = "Question links list cannot be empty")
    @Valid
    private List<QuestionLinkCreateDto> questionLinks;
}