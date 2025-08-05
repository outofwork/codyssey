package com.codyssey.api.dto.question;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for company information with frequency and year
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyInfoDto {
    private String companyLabelId;
    private String companyName;
    private Integer frequency;
    private Integer lastAskedYear;
}