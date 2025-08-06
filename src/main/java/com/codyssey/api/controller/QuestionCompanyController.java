package com.codyssey.api.controller;

import com.codyssey.api.dto.question.QuestionCompanyCreateDto;
import com.codyssey.api.service.CodingQuestionService;
import com.codyssey.api.validation.ValidId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for QuestionCompany relationship management
 * <p>
 * Provides endpoints for managing question-company relationships.
 */
@RestController
@RequestMapping("/v1/question-companies")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Question Company Management", description = "APIs for managing question-company relationships")
public class QuestionCompanyController {

    private final CodingQuestionService codingQuestionService;

    @Operation(summary = "Add company to question", description = "Creates a relationship between a question and a company")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Question-company relationship created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Question or company label not found"),
            @ApiResponse(responseCode = "409", description = "Question-company relationship already exists")
    })
    @PostMapping
    public ResponseEntity<Void> addCompanyToQuestion(
            @Parameter(description = "Question-company relationship data", required = true)
            @Valid @RequestBody QuestionCompanyCreateDto createDto) {

        log.info("POST /v1/question-companies - Adding company {} to question {}", 
                createDto.getCompanyLabelId(), createDto.getQuestionId());
        codingQuestionService.addCompanyToQuestion(createDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Remove company from question", description = "Removes a relationship between a question and a company")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Question-company relationship removed successfully"),
            @ApiResponse(responseCode = "404", description = "Question-company relationship not found")
    })
    @DeleteMapping("/question/{questionId}/company/{companyLabelId}")
    public ResponseEntity<Void> removeCompanyFromQuestion(
            @Parameter(description = "Question ID", required = true)
            @PathVariable @ValidId String questionId,
            @Parameter(description = "Company Label ID", required = true)
            @PathVariable @ValidId String companyLabelId) {

        log.info("DELETE /v1/question-companies/question/{}/company/{} - Removing company from question", 
                questionId, companyLabelId);
        codingQuestionService.removeCompanyFromQuestion(questionId, companyLabelId);
        return ResponseEntity.noContent().build();
    }
}