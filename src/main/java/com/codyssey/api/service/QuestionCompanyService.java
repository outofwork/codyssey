package com.codyssey.api.service;

import com.codyssey.api.dto.question.QuestionCompanyCreateDto;
import com.codyssey.api.dto.question.QuestionCompanyBulkCreateDto;
import com.codyssey.api.dto.question.CompanyInfoDto;
import com.codyssey.api.dto.label.LabelSummaryDto;

import java.util.List;

/**
 * Service interface for QuestionCompany operations
 * <p>
 * Defines the contract for question-company association business logic operations.
 */
public interface QuestionCompanyService {

    /**
     * Create a new question-company association
     *
     * @param createDto association creation data
     * @return success message
     */
    String createQuestionCompany(QuestionCompanyCreateDto createDto);

    /**
     * Create multiple question-company associations in bulk
     *
     * @param bulkCreateDto bulk association creation data
     * @return list of success/error messages
     */
    List<String> createQuestionCompaniesBulk(QuestionCompanyBulkCreateDto bulkCreateDto);

    /**
     * Get all companies for a question
     *
     * @param questionId the question ID
     * @return list of companies associated with the question
     */
    List<CompanyInfoDto> getCompaniesByQuestionId(String questionId);

    /**
     * Get all question IDs for a company
     *
     * @param companyLabelId the company label ID
     * @return list of question IDs
     */
    List<String> getQuestionIdsByCompanyLabelId(String companyLabelId);

    /**
     * Remove a company from a question
     *
     * @param questionId the question ID
     * @param companyLabelId the company label ID
     */
    void removeQuestionCompany(String questionId, String companyLabelId);

    /**
     * Remove all companies from a question
     *
     * @param questionId the question ID
     */
    void removeAllCompaniesFromQuestion(String questionId);

    /**
     * Check if a question-company association exists
     *
     * @param questionId the question ID
     * @param companyLabelId the company label ID
     * @return true if association exists
     */
    boolean isQuestionCompanyExists(String questionId, String companyLabelId);

    /**
     * Update frequency for a question-company association
     *
     * @param questionId the question ID
     * @param companyLabelId the company label ID
     * @param frequency the new frequency
     * @return updated company info
     */
    CompanyInfoDto updateQuestionCompanyFrequency(String questionId, String companyLabelId, Integer frequency);

    /**
     * Update last asked year for a question-company association
     *
     * @param questionId the question ID
     * @param companyLabelId the company label ID
     * @param lastAskedYear the new year
     * @return updated company info
     */
    CompanyInfoDto updateQuestionCompanyYear(String questionId, String companyLabelId, Integer lastAskedYear);

    /**
     * Get questions by company and year
     *
     * @param companyLabelId the company label ID
     * @param year the year
     * @return list of question IDs
     */
    List<String> getQuestionsByCompanyAndYear(String companyLabelId, Integer year);

    /**
     * Get top companies by frequency
     *
     * @param limit maximum number of companies to return
     * @return list of top companies
     */
    List<LabelSummaryDto> getTopCompaniesByFrequency(int limit);

    /**
     * Get count of companies for a question
     *
     * @param questionId the question ID
     * @return number of companies
     */
    Long getCompaniesCountByQuestionId(String questionId);

    /**
     * Get count of questions for a company
     *
     * @param companyLabelId the company label ID
     * @return number of questions
     */
    Long getQuestionsCountByCompanyLabelId(String companyLabelId);

    /**
     * Replace all companies for a question
     *
     * @param questionId the question ID
     * @param companyData list of new company data
     * @return list of new company info
     */
    List<CompanyInfoDto> replaceQuestionCompanies(String questionId, List<QuestionCompanyCreateDto> companyData);
}