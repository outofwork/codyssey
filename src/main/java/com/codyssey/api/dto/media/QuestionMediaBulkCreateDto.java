package com.codyssey.api.dto.media;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Data Transfer Object for bulk creating QuestionMedia
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionMediaBulkCreateDto {

    @NotEmpty(message = "Media list cannot be empty")
    @Valid
    private List<QuestionMediaCreateDto> mediaList;
}