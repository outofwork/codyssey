package com.codyssey.api.dto.systemdesign;

import com.codyssey.api.validation.ValidId;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for creating SystemDesignLabel associations
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemDesignLabelCreateDto {

    @NotBlank(message = "System design ID is required")
    @ValidId
    private String systemDesignId;

    @NotBlank(message = "Label ID is required")
    @ValidId
    private String labelId;

    private Boolean isPrimary = false;

    private Integer displayOrder;
}
