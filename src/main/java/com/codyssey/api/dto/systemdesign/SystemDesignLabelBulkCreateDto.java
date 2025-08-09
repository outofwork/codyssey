package com.codyssey.api.dto.systemdesign;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Data Transfer Object for creating multiple SystemDesignLabel associations in bulk
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemDesignLabelBulkCreateDto {

    @NotEmpty(message = "System design label list cannot be empty")
    @Valid
    private List<SystemDesignLabelCreateDto> systemDesignLabels;
}
