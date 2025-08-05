package com.codyssey.api.service.impl;

import com.codyssey.api.dto.question.QuestionCompanyCreateDto;
import com.codyssey.api.dto.question.QuestionCompanyBulkCreateDto;
import com.codyssey.api.dto.label.LabelSummaryDto;
import com.codyssey.api.exception.DuplicateResourceException;
import com.codyssey.api.exception.ResourceNotFoundException;
import com.codyssey.api.model.*;
import com.codyssey.api.repository.*;
import com.codyssey.api.service.QuestionCompanyService;
import com.codyssey.api.dto.question.CompanyInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of QuestionCompanyService
 * <p>
 * Provides question-company association management functionality including
 * bulk operations, frequency tracking, and company analytics.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class QuestionCompanyServiceImpl implements QuestionCompanyService {

    private final QuestionCompanyRepository questionCompanyRepository;
    private final CodingQuestionRepository questionRepository;
    private final LabelRepository labelRepository;

    @Override
    public String createQuestionCompany(QuestionCompanyCreateDto createDto) {
        log.info("Creating question-company association: question={}, company={}", 
                createDto.getQuestionId(), createDto.getCompanyLabelId());

        // Validate question exists
        CodingQuestion question = questionRepository.findByIdAndNotDeleted(createDto.getQuestionId())
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with ID: " + createDto.getQuestionId()));

        // Validate company label exists
        Label companyLabel = labelRepository.findByIdAndNotDeleted(createDto.getCompanyLabelId())
                .orElseThrow(() -> new ResourceNotFoundException("Company label not found with ID: " + createDto.getCompanyLabelId()));

        // Check if association already exists
        if (questionCompanyRepository.existsByQuestionIdAndCompanyLabelId(createDto.getQuestionId(), createDto.getCompanyLabelId())) {
            throw new DuplicateResourceException("Question-company association already exists");
        }

        // Create the association
        QuestionCompany questionCompany = new QuestionCompany(question, companyLabel, 
                createDto.getFrequency(), createDto.getLastAskedYear());
        questionCompanyRepository.save(questionCompany);

        log.info("Successfully created question-company association with ID: {}", questionCompany.getId());
        return "Association created successfully between question " + createDto.getQuestionId() + 
               " and company " + createDto.getCompanyLabelId();
    }

    @Override
    public List<String> createQuestionCompaniesBulk(QuestionCompanyBulkCreateDto bulkCreateDto) {
        log.info("Creating {} question-company associations in bulk", bulkCreateDto.getQuestionCompanies().size());

        List<String> results = new ArrayList<>();

        for (QuestionCompanyCreateDto createDto : bulkCreateDto.getQuestionCompanies()) {
            try {
                String result = createQuestionCompany(createDto);
                results.add(result);
            } catch (Exception e) {
                log.error("Failed to create association for question: {}, company: {} - {}", 
                        createDto.getQuestionId(), createDto.getCompanyLabelId(), e.getMessage());
                results.add("Failed: " + e.getMessage());
            }
        }

        log.info("Completed bulk creation of question-company associations");
        return results;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompanyInfoDto> getCompaniesByQuestionId(String questionId) {
        log.info("Retrieving companies for question ID: {}", questionId);
        List<QuestionCompany> questionCompanies = questionCompanyRepository.findByQuestionId(questionId);
        return questionCompanies.stream()
                .map(this::convertToCompanyInfoDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getQuestionIdsByCompanyLabelId(String companyLabelId) {
        log.info("Retrieving question IDs for company label ID: {}", companyLabelId);
        List<CodingQuestion> questions = questionCompanyRepository.findQuestionsByCompanyLabelId(companyLabelId);
        return questions.stream()
                .map(CodingQuestion::getId)
                .collect(Collectors.toList());
    }

    @Override
    public void removeQuestionCompany(String questionId, String companyLabelId) {
        log.info("Removing company {} from question {}", companyLabelId, questionId);

        QuestionCompany questionCompany = questionCompanyRepository.findByQuestionIdAndCompanyLabelId(questionId, companyLabelId)
                .orElseThrow(() -> new ResourceNotFoundException("Question-company association not found"));

        questionCompanyRepository.delete(questionCompany);
        log.info("Successfully removed company {} from question {}", companyLabelId, questionId);
    }

    @Override
    public void removeAllCompaniesFromQuestion(String questionId) {
        log.info("Removing all companies from question: {}", questionId);

        // Validate question exists
        if (!questionRepository.existsById(questionId)) {
            throw new ResourceNotFoundException("Question not found with ID: " + questionId);
        }

        questionCompanyRepository.deleteByQuestionId(questionId);
        log.info("Successfully removed all companies from question: {}", questionId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isQuestionCompanyExists(String questionId, String companyLabelId) {
        log.info("Checking if question-company association exists: question={}, company={}", questionId, companyLabelId);
        return questionCompanyRepository.existsByQuestionIdAndCompanyLabelId(questionId, companyLabelId);
    }

    @Override
    public CompanyInfoDto updateQuestionCompanyFrequency(String questionId, String companyLabelId, Integer frequency) {
        log.info("Updating frequency for question {} and company {} to {}", questionId, companyLabelId, frequency);

        QuestionCompany questionCompany = questionCompanyRepository.findByQuestionIdAndCompanyLabelId(questionId, companyLabelId)
                .orElseThrow(() -> new ResourceNotFoundException("Question-company association not found"));

        questionCompany.setFrequency(frequency);
        QuestionCompany savedQuestionCompany = questionCompanyRepository.save(questionCompany);

        log.info("Successfully updated frequency for question-company association");
        return convertToCompanyInfoDto(savedQuestionCompany);
    }

    @Override
    public CompanyInfoDto updateQuestionCompanyYear(String questionId, String companyLabelId, Integer lastAskedYear) {
        log.info("Updating last asked year for question {} and company {} to {}", questionId, companyLabelId, lastAskedYear);

        QuestionCompany questionCompany = questionCompanyRepository.findByQuestionIdAndCompanyLabelId(questionId, companyLabelId)
                .orElseThrow(() -> new ResourceNotFoundException("Question-company association not found"));

        questionCompany.setLastAskedYear(lastAskedYear);
        QuestionCompany savedQuestionCompany = questionCompanyRepository.save(questionCompany);

        log.info("Successfully updated last asked year for question-company association");
        return convertToCompanyInfoDto(savedQuestionCompany);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getQuestionsByCompanyAndYear(String companyLabelId, Integer year) {
        log.info("Retrieving questions for company {} in year {}", companyLabelId, year);
        List<CodingQuestion> questions = questionCompanyRepository.findQuestionsByCompanyAndYear(companyLabelId, year);
        return questions.stream()
                .map(CodingQuestion::getId)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabelSummaryDto> getTopCompaniesByFrequency(int limit) {
        log.info("Getting {} top companies by frequency", limit);
        List<Label> topCompanies = questionCompanyRepository.findTopCompaniesByFrequency(limit);
        return topCompanies.stream()
                .map(this::convertToLabelSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Long getCompaniesCountByQuestionId(String questionId) {
        log.info("Getting companies count for question: {}", questionId);
        return questionCompanyRepository.countByQuestionId(questionId);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getQuestionsCountByCompanyLabelId(String companyLabelId) {
        log.info("Getting questions count for company: {}", companyLabelId);
        return questionCompanyRepository.countByCompanyLabelId(companyLabelId);
    }

    @Override
    public List<CompanyInfoDto> replaceQuestionCompanies(String questionId, List<QuestionCompanyCreateDto> companyData) {
        log.info("Replacing all companies for question {} with {} new companies", questionId, companyData.size());

        // Validate question exists
        CodingQuestion question = questionRepository.findByIdAndNotDeleted(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with ID: " + questionId));

        // Remove all existing associations
        questionCompanyRepository.deleteByQuestionId(questionId);

        // Create new associations
        List<CompanyInfoDto> results = new ArrayList<>();
        for (QuestionCompanyCreateDto createDto : companyData) {
            // Override questionId to ensure consistency
            createDto.setQuestionId(questionId);
            
            Label companyLabel = labelRepository.findByIdAndNotDeleted(createDto.getCompanyLabelId())
                    .orElseThrow(() -> new ResourceNotFoundException("Company label not found with ID: " + createDto.getCompanyLabelId()));

            QuestionCompany questionCompany = new QuestionCompany(question, companyLabel, 
                    createDto.getFrequency(), createDto.getLastAskedYear());
            QuestionCompany saved = questionCompanyRepository.save(questionCompany);
            results.add(convertToCompanyInfoDto(saved));
        }

        log.info("Successfully replaced companies for question: {}", questionId);
        return results;
    }

    // Helper methods for conversion
    private CompanyInfoDto convertToCompanyInfoDto(QuestionCompany questionCompany) {
        CompanyInfoDto dto = new CompanyInfoDto();
        dto.setCompanyLabelId(questionCompany.getCompanyLabel().getId());
        dto.setCompanyName(questionCompany.getCompanyLabel().getName());
        dto.setFrequency(questionCompany.getFrequency());
        dto.setLastAskedYear(questionCompany.getLastAskedYear());
        return dto;
    }

    private LabelSummaryDto convertToLabelSummaryDto(Label label) {
        LabelSummaryDto dto = new LabelSummaryDto();
        dto.setId(label.getId());
        dto.setName(label.getName());
        dto.setDescription(label.getDescription());
        if (label.getCategory() != null) {
            dto.setCategoryCode(label.getCategory().getCode());
        }
        return dto;
    }
}