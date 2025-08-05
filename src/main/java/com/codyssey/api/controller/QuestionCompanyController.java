package com.codyssey.api.controller;

import com.codyssey.api.dto.question.QuestionCompanyCreateDto;
import com.codyssey.api.dto.question.QuestionCompanyBulkCreateDto;
import com.codyssey.api.dto.label.LabelSummaryDto;
import com.codyssey.api.service.QuestionCompanyService;
import com.codyssey.api.dto.question.CompanyInfoDto;
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

import java.util.List;

/**
 * REST Controller for QuestionCompany operations
 * <p>
 * Provides endpoints for managing question-company associations including bulk operations
 * for tracking which companies ask specific coding questions in interviews.
 */
@RestController
@RequestMapping("/v1/question-companies")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Question Company Management", description = "APIs for managing question-company associations (interview tracking)")
public class QuestionCompanyController {

    private final QuestionCompanyService questionCompanyService;

    @Operation(summary = "Create question-company association", description = "Associates a company with a coding question they ask in interviews")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Association created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Question or company label not found"),
            @ApiResponse(responseCode = "409", description = "Association already exists")
    })
    @PostMapping
    public ResponseEntity<String> createQuestionCompany(
            @Parameter(description = "Question-company association data", required = true)
            @Valid @RequestBody QuestionCompanyCreateDto createDto) {

        log.info("POST /v1/question-companies - Creating association: question={}, company={}", 
                createDto.getQuestionId(), createDto.getCompanyLabelId());
        String result = questionCompanyService.createQuestionCompany(createDto);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @Operation(summary = "Create multiple question-company associations in bulk", 
            description = "Associates multiple companies with questions in a single operation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Associations created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "One or more questions or company labels not found"),
            @ApiResponse(responseCode = "409", description = "One or more associations already exist")
    })
    @PostMapping("/bulk")
    public ResponseEntity<List<String>> createQuestionCompaniesBulk(
            @Parameter(description = "Bulk question-company association data", required = true)
            @Valid @RequestBody QuestionCompanyBulkCreateDto bulkCreateDto) {

        log.info("POST /v1/question-companies/bulk - Creating {} associations in bulk", 
                bulkCreateDto.getQuestionCompanies().size());
        List<String> results = questionCompanyService.createQuestionCompaniesBulk(bulkCreateDto);
        return new ResponseEntity<>(results, HttpStatus.CREATED);
    }

    @Operation(summary = "Get companies for question", description = "Retrieves all companies that ask a specific question")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Companies retrieved successfully")
    })
    @GetMapping("/question/{questionId}/companies")
    public ResponseEntity<List<CompanyInfoDto>> getCompaniesByQuestionId(
            @Parameter(description = "Question ID", required = true)
            @PathVariable @ValidId String questionId) {

        log.info("GET /v1/question-companies/question/{}/companies - Retrieving companies for question", questionId);
        List<CompanyInfoDto> companies = questionCompanyService.getCompaniesByQuestionId(questionId);
        return ResponseEntity.ok(companies);
    }

    @Operation(summary = "Get questions for company", description = "Retrieves all question IDs asked by a specific company")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Question IDs retrieved successfully")
    })
    @GetMapping("/company/{companyLabelId}/questions")
    public ResponseEntity<List<String>> getQuestionIdsByCompanyLabelId(
            @Parameter(description = "Company label ID", required = true)
            @PathVariable @ValidId String companyLabelId) {

        log.info("GET /v1/question-companies/company/{}/questions - Retrieving questions for company", companyLabelId);
        List<String> questionIds = questionCompanyService.getQuestionIdsByCompanyLabelId(companyLabelId);
        return ResponseEntity.ok(questionIds);
    }

    @Operation(summary = "Remove company from question", description = "Removes a specific company association from a question")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Association removed successfully"),
            @ApiResponse(responseCode = "404", description = "Association not found")
    })
    @DeleteMapping("/question/{questionId}/company/{companyLabelId}")
    public ResponseEntity<Void> removeQuestionCompany(
            @Parameter(description = "Question ID", required = true)
            @PathVariable @ValidId String questionId,
            @Parameter(description = "Company label ID", required = true)
            @PathVariable @ValidId String companyLabelId) {

        log.info("DELETE /v1/question-companies/question/{}/company/{} - Removing company from question", questionId, companyLabelId);
        questionCompanyService.removeQuestionCompany(questionId, companyLabelId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Remove all companies from question", description = "Removes all company associations from a question")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "All companies removed successfully")
    })
    @DeleteMapping("/question/{questionId}/companies")
    public ResponseEntity<Void> removeAllCompaniesFromQuestion(
            @Parameter(description = "Question ID", required = true)
            @PathVariable @ValidId String questionId) {

        log.info("DELETE /v1/question-companies/question/{}/companies - Removing all companies from question", questionId);
        questionCompanyService.removeAllCompaniesFromQuestion(questionId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Check if association exists", description = "Checks if a question-company association exists")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Check completed successfully")
    })
    @GetMapping("/exists")
    public ResponseEntity<Boolean> isQuestionCompanyExists(
            @Parameter(description = "Question ID", required = true)
            @RequestParam @ValidId String questionId,
            @Parameter(description = "Company label ID", required = true)
            @RequestParam @ValidId String companyLabelId) {

        log.info("GET /v1/question-companies/exists?questionId={}&companyLabelId={} - Checking association existence", questionId, companyLabelId);
        boolean exists = questionCompanyService.isQuestionCompanyExists(questionId, companyLabelId);
        return ResponseEntity.ok(exists);
    }

    @Operation(summary = "Update frequency", description = "Updates how frequently a company asks a specific question")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Frequency updated successfully"),
            @ApiResponse(responseCode = "404", description = "Association not found")
    })
    @PutMapping("/question/{questionId}/company/{companyLabelId}/frequency")
    public ResponseEntity<CompanyInfoDto> updateQuestionCompanyFrequency(
            @Parameter(description = "Question ID", required = true)
            @PathVariable @ValidId String questionId,
            @Parameter(description = "Company label ID", required = true)
            @PathVariable @ValidId String companyLabelId,
            @Parameter(description = "New frequency value", required = true)
            @RequestParam Integer frequency) {

        log.info("PUT /v1/question-companies/question/{}/company/{}/frequency?frequency={} - Updating frequency", 
                questionId, companyLabelId, frequency);
        CompanyInfoDto updatedInfo = questionCompanyService.updateQuestionCompanyFrequency(questionId, companyLabelId, frequency);
        return ResponseEntity.ok(updatedInfo);
    }

    @Operation(summary = "Update last asked year", description = "Updates the year when a company last asked a specific question")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Last asked year updated successfully"),
            @ApiResponse(responseCode = "404", description = "Association not found")
    })
    @PutMapping("/question/{questionId}/company/{companyLabelId}/year")
    public ResponseEntity<CompanyInfoDto> updateQuestionCompanyYear(
            @Parameter(description = "Question ID", required = true)
            @PathVariable @ValidId String questionId,
            @Parameter(description = "Company label ID", required = true)
            @PathVariable @ValidId String companyLabelId,
            @Parameter(description = "Last asked year", required = true)
            @RequestParam Integer lastAskedYear) {

        log.info("PUT /v1/question-companies/question/{}/company/{}/year?lastAskedYear={} - Updating last asked year", 
                questionId, companyLabelId, lastAskedYear);
        CompanyInfoDto updatedInfo = questionCompanyService.updateQuestionCompanyYear(questionId, companyLabelId, lastAskedYear);
        return ResponseEntity.ok(updatedInfo);
    }

    @Operation(summary = "Get questions by company and year", description = "Retrieves questions asked by a company in a specific year")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Questions retrieved successfully")
    })
    @GetMapping("/company/{companyLabelId}/year/{year}/questions")
    public ResponseEntity<List<String>> getQuestionsByCompanyAndYear(
            @Parameter(description = "Company label ID", required = true)
            @PathVariable @ValidId String companyLabelId,
            @Parameter(description = "Year", required = true)
            @PathVariable Integer year) {

        log.info("GET /v1/question-companies/company/{}/year/{}/questions - Retrieving questions by company and year", companyLabelId, year);
        List<String> questionIds = questionCompanyService.getQuestionsByCompanyAndYear(companyLabelId, year);
        return ResponseEntity.ok(questionIds);
    }

    @Operation(summary = "Get top companies", description = "Retrieves top companies by frequency of questions asked")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Top companies retrieved successfully")
    })
    @GetMapping("/top-companies")
    public ResponseEntity<List<LabelSummaryDto>> getTopCompaniesByFrequency(
            @Parameter(description = "Maximum number of companies to return", required = false)
            @RequestParam(defaultValue = "10") int limit) {

        log.info("GET /v1/question-companies/top-companies?limit={} - Getting top companies", limit);
        List<LabelSummaryDto> topCompanies = questionCompanyService.getTopCompaniesByFrequency(limit);
        return ResponseEntity.ok(topCompanies);
    }

    @Operation(summary = "Get companies count for question", description = "Gets the count of companies that ask a specific question")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Company count retrieved successfully")
    })
    @GetMapping("/question/{questionId}/count")
    public ResponseEntity<Long> getCompaniesCountByQuestionId(
            @Parameter(description = "Question ID", required = true)
            @PathVariable @ValidId String questionId) {

        log.info("GET /v1/question-companies/question/{}/count - Getting companies count", questionId);
        Long count = questionCompanyService.getCompaniesCountByQuestionId(questionId);
        return ResponseEntity.ok(count);
    }

    @Operation(summary = "Get questions count for company", description = "Gets the count of questions asked by a specific company")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Question count retrieved successfully")
    })
    @GetMapping("/company/{companyLabelId}/count")
    public ResponseEntity<Long> getQuestionsCountByCompanyLabelId(
            @Parameter(description = "Company label ID", required = true)
            @PathVariable @ValidId String companyLabelId) {

        log.info("GET /v1/question-companies/company/{}/count - Getting questions count", companyLabelId);
        Long count = questionCompanyService.getQuestionsCountByCompanyLabelId(companyLabelId);
        return ResponseEntity.ok(count);
    }

    @Operation(summary = "Replace all companies for question", description = "Replaces all companies for a question with a new set")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Companies replaced successfully"),
            @ApiResponse(responseCode = "404", description = "Question or one or more company labels not found")
    })
    @PutMapping("/question/{questionId}/companies")
    public ResponseEntity<List<CompanyInfoDto>> replaceQuestionCompanies(
            @Parameter(description = "Question ID", required = true)
            @PathVariable @ValidId String questionId,
            @Parameter(description = "List of new company data", required = true)
            @RequestBody List<QuestionCompanyCreateDto> companyData) {

        log.info("PUT /v1/question-companies/question/{}/companies - Replacing companies for question", questionId);
        List<CompanyInfoDto> companies = questionCompanyService.replaceQuestionCompanies(questionId, companyData);
        return ResponseEntity.ok(companies);
    }
}