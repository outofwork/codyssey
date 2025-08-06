package com.codyssey.api.service.impl;

import com.codyssey.api.dto.question.*;
import com.codyssey.api.dto.label.LabelSummaryDto;
import com.codyssey.api.dto.source.SourceSummaryDto;
import com.codyssey.api.exception.DuplicateResourceException;
import com.codyssey.api.exception.ResourceNotFoundException;
import com.codyssey.api.model.*;
import com.codyssey.api.repository.*;
import com.codyssey.api.service.CodingQuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of CodingQuestionService
 * <p>
 * Provides coding question management functionality including
 * creation, retrieval, updating, and deletion with comprehensive search capabilities.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CodingQuestionServiceImpl implements CodingQuestionService {

    private final CodingQuestionRepository codingQuestionRepository;
    private final LabelRepository labelRepository;
    private final UserRepository userRepository;
    private final SourceRepository sourceRepository;
    private final QuestionLabelRepository questionLabelRepository;
    private final QuestionCompanyRepository questionCompanyRepository;

    @Override
    public CodingQuestionDto createQuestion(CodingQuestionCreateDto createDto) {
        log.info("Creating new coding question with title: {}", createDto.getTitle());

        // Validate difficulty label if provided
        Label difficultyLabel = null;
        if (createDto.getDifficultyLabelId() != null && !createDto.getDifficultyLabelId().trim().isEmpty()) {
            difficultyLabel = labelRepository.findByIdAndNotDeleted(createDto.getDifficultyLabelId())
                    .orElseThrow(() -> new ResourceNotFoundException("Difficulty label not found with ID: " + createDto.getDifficultyLabelId()));
        }

        // Validate source if provided
        Source source = null;
        if (createDto.getSourceId() != null && !createDto.getSourceId().trim().isEmpty()) {
            source = sourceRepository.findByIdAndNotDeleted(createDto.getSourceId())
                    .orElseThrow(() -> new ResourceNotFoundException("Source not found with ID: " + createDto.getSourceId()));
        }

        // Validate user if provided
        User createdByUser = null;
        if (createDto.getCreatedByUserId() != null && !createDto.getCreatedByUserId().trim().isEmpty()) {
            createdByUser = userRepository.findByIdAndNotDeleted(createDto.getCreatedByUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + createDto.getCreatedByUserId()));
        }

        // Check for duplicate title within source
        if (source != null) {
            if (codingQuestionRepository.existsByTitleAndSourceId(createDto.getTitle(), source.getId())) {
                throw new DuplicateResourceException("Question with title '" + createDto.getTitle() + 
                        "' already exists for source: " + source.getName());
            }
        }

        // Check for duplicate platform question ID within source
        if (source != null && createDto.getPlatformQuestionId() != null) {
            if (codingQuestionRepository.existsByPlatformQuestionIdAndSourceId(createDto.getPlatformQuestionId(), source.getId())) {
                throw new DuplicateResourceException("Question with platform ID '" + createDto.getPlatformQuestionId() + 
                        "' already exists for source: " + source.getName());
            }
        }

        // Create the question entity
        CodingQuestion question = new CodingQuestion();
        question.setTitle(createDto.getTitle());
        question.setShortDescription(createDto.getShortDescription());
        question.setFilePath(createDto.getFilePath());
        question.setDifficultyLabel(difficultyLabel);
        question.setSource(source);
        question.setPlatformQuestionId(createDto.getPlatformQuestionId());
        question.setOriginalUrl(createDto.getOriginalUrl());
        question.setStatus(CodingQuestion.QuestionStatus.valueOf(createDto.getStatus()));
        question.setCreatedByUser(createdByUser);

        CodingQuestion savedQuestion = codingQuestionRepository.save(question);
        log.info("Successfully created coding question with ID: {}", savedQuestion.getId());

        return convertToDto(savedQuestion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CodingQuestionSummaryDto> getAllQuestions() {
        log.info("Retrieving all coding questions");
        List<CodingQuestion> questions = codingQuestionRepository.findByDeletedFalse();
        return questions.stream()
                .map(this::convertToSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CodingQuestionDto> getQuestionById(String id) {
        log.info("Retrieving coding question by ID: {}", id);
        return codingQuestionRepository.findByIdAndNotDeleted(id)
                .map(this::convertToDto);
    }

    @Override
    public CodingQuestionDto updateQuestion(String id, CodingQuestionUpdateDto updateDto) {
        log.info("Updating coding question with ID: {}", id);

        CodingQuestion question = codingQuestionRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coding question not found with ID: " + id));

        // Update fields if provided
        if (updateDto.getTitle() != null) {
            question.setTitle(updateDto.getTitle());
        }
        if (updateDto.getShortDescription() != null) {
            question.setShortDescription(updateDto.getShortDescription());
        }
        if (updateDto.getFilePath() != null) {
            question.setFilePath(updateDto.getFilePath());
        }
        if (updateDto.getDifficultyLabelId() != null) {
            Label difficultyLabel = labelRepository.findByIdAndNotDeleted(updateDto.getDifficultyLabelId())
                    .orElseThrow(() -> new ResourceNotFoundException("Difficulty label not found with ID: " + updateDto.getDifficultyLabelId()));
            question.setDifficultyLabel(difficultyLabel);
        }
        if (updateDto.getSourceId() != null) {
            Source source = sourceRepository.findByIdAndNotDeleted(updateDto.getSourceId())
                    .orElseThrow(() -> new ResourceNotFoundException("Source not found with ID: " + updateDto.getSourceId()));
            question.setSource(source);
        }
        if (updateDto.getPlatformQuestionId() != null) {
            question.setPlatformQuestionId(updateDto.getPlatformQuestionId());
        }
        if (updateDto.getOriginalUrl() != null) {
            question.setOriginalUrl(updateDto.getOriginalUrl());
        }
        if (updateDto.getStatus() != null) {
            question.setStatus(CodingQuestion.QuestionStatus.valueOf(updateDto.getStatus()));
        }

        CodingQuestion savedQuestion = codingQuestionRepository.save(question);
        log.info("Successfully updated coding question with ID: {}", savedQuestion.getId());

        return convertToDto(savedQuestion);
    }

    @Override
    public void deleteQuestion(String id) {
        log.info("Deleting coding question with ID: {}", id);
        CodingQuestion question = codingQuestionRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coding question not found with ID: " + id));
        
        question.setDeleted(true);
        codingQuestionRepository.save(question);
        log.info("Successfully deleted coding question with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CodingQuestionSummaryDto> getQuestionsWithPagination(Pageable pageable) {
        log.info("Retrieving coding questions with pagination");
        Page<CodingQuestion> questions = codingQuestionRepository.findQuestionsWithPagination(pageable);
        return questions.map(this::convertToSummaryDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CodingQuestionSummaryDto> searchQuestions(String searchTerm, Pageable pageable) {
        log.info("Searching coding questions with term: {}", searchTerm);
        Page<CodingQuestion> questions = codingQuestionRepository.searchByTitleOrDescription(searchTerm, pageable);
        return questions.map(this::convertToSummaryDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CodingQuestionSummaryDto> getQuestionsBySource(String sourceId) {
        log.info("Retrieving questions by source: {}", sourceId);
        List<CodingQuestion> questions = codingQuestionRepository.findBySourceId(sourceId);
        return questions.stream()
                .map(this::convertToSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CodingQuestionSummaryDto> getQuestionsByDifficulty(String difficultyLabelId) {
        log.info("Retrieving questions by difficulty: {}", difficultyLabelId);
        Label difficultyLabel = labelRepository.findByIdAndNotDeleted(difficultyLabelId)
                .orElseThrow(() -> new ResourceNotFoundException("Difficulty label not found with ID: " + difficultyLabelId));
        
        List<CodingQuestion> questions = codingQuestionRepository.findByDifficultyLabel(difficultyLabel);
        return questions.stream()
                .map(this::convertToSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CodingQuestionSummaryDto> getQuestionsByLabel(String labelId) {
        log.info("Retrieving questions by label: {}", labelId);
        List<CodingQuestion> questions = codingQuestionRepository.findByLabelId(labelId);
        return questions.stream()
                .map(this::convertToSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CodingQuestionSummaryDto> getQuestionsByCompany(String companyLabelId) {
        log.info("Retrieving questions by company: {}", companyLabelId);
        List<CodingQuestion> questions = codingQuestionRepository.findByCompanyLabelId(companyLabelId);
        return questions.stream()
                .map(this::convertToSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CodingQuestionSummaryDto> searchWithFilters(List<String> sourceIds,
                                                            List<String> difficultyIds,
                                                            List<String> labelIds,
                                                            List<String> companyIds,
                                                            String searchTerm) {
        log.info("Searching questions with filters - sources: {}, difficulties: {}, labels: {}, companies: {}, term: {}",
                sourceIds, difficultyIds, labelIds, companyIds, searchTerm);
        
        List<CodingQuestion> questions = codingQuestionRepository.findWithFilters(
                sourceIds, difficultyIds, labelIds, companyIds, searchTerm);
        
        return questions.stream()
                .map(this::convertToSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public QuestionStatisticsDto getQuestionStatistics(String questionId) {
        log.info("Retrieving statistics for question ID: {}", questionId);

        if (!codingQuestionRepository.existsById(questionId)) {
            throw new ResourceNotFoundException("Coding question not found with ID: " + questionId);
        }

        Long labelsCount = questionLabelRepository.countByQuestionId(questionId);
        Long companiesCount = questionCompanyRepository.countByQuestionId(questionId);
        Long outgoingLinksCount = 0L; // Will be implemented when QuestionLink entity is added
        Long incomingLinksCount = 0L;  // Will be implemented when QuestionLink entity is added

        return new QuestionStatisticsDto(questionId, labelsCount, companiesCount, outgoingLinksCount, incomingLinksCount);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkTitleAvailability(String title, String sourceId) {
        log.info("Checking title availability: {} for source: {}", title, sourceId);
        return !codingQuestionRepository.existsByTitleAndSourceId(title, sourceId);
    }

    @Override
    public void addLabelToQuestion(QuestionLabelCreateDto createDto) {
        log.info("Adding label {} to question {}", createDto.getLabelId(), createDto.getQuestionId());
        
        // Validate question exists
        CodingQuestion question = codingQuestionRepository.findByIdAndNotDeleted(createDto.getQuestionId())
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with ID: " + createDto.getQuestionId()));
        
        // Validate label exists
        Label label = labelRepository.findByIdAndNotDeleted(createDto.getLabelId())
                .orElseThrow(() -> new ResourceNotFoundException("Label not found with ID: " + createDto.getLabelId()));
        
        // Check if relationship already exists
        if (questionLabelRepository.existsByQuestionIdAndLabelId(createDto.getQuestionId(), createDto.getLabelId())) {
            throw new DuplicateResourceException("Question-label relationship already exists");
        }
        
        QuestionLabel questionLabel = new QuestionLabel(question, label, 
                createDto.getRelevanceScore(), createDto.getIsPrimary());
        questionLabel.setNotes(createDto.getNotes());
        
        questionLabelRepository.save(questionLabel);
        log.info("Successfully added label to question");
    }

    @Override
    public void addLabelsToQuestion(QuestionLabelBulkCreateDto bulkCreateDto) {
        log.info("Adding {} labels to question {}", 
                bulkCreateDto.getLabelAssignments().size(), bulkCreateDto.getQuestionId());
        
        // Validate question exists
        CodingQuestion question = codingQuestionRepository.findByIdAndNotDeleted(bulkCreateDto.getQuestionId())
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with ID: " + bulkCreateDto.getQuestionId()));
        
        for (QuestionLabelBulkCreateDto.LabelAssignment assignment : bulkCreateDto.getLabelAssignments()) {
            // Validate label exists
            Label label = labelRepository.findByIdAndNotDeleted(assignment.getLabelId())
                    .orElseThrow(() -> new ResourceNotFoundException("Label not found with ID: " + assignment.getLabelId()));
            
            // Skip if relationship already exists
            if (!questionLabelRepository.existsByQuestionIdAndLabelId(bulkCreateDto.getQuestionId(), assignment.getLabelId())) {
                QuestionLabel questionLabel = new QuestionLabel(question, label, 
                        assignment.getRelevanceScore(), assignment.getIsPrimary());
                questionLabel.setNotes(assignment.getNotes());
                questionLabelRepository.save(questionLabel);
            }
        }
        log.info("Successfully added labels to question");
    }

    @Override
    public void removeLabelFromQuestion(String questionId, String labelId) {
        log.info("Removing label {} from question {}", labelId, questionId);
        
        QuestionLabel questionLabel = questionLabelRepository.findByQuestionIdAndLabelId(questionId, labelId)
                .orElseThrow(() -> new ResourceNotFoundException("Question-label relationship not found"));
        
        questionLabel.setDeleted(true);
        questionLabelRepository.save(questionLabel);
        log.info("Successfully removed label from question");
    }

    @Override
    public void addCompanyToQuestion(QuestionCompanyCreateDto createDto) {
        log.info("Adding company {} to question {}", createDto.getCompanyLabelId(), createDto.getQuestionId());
        
        // Validate question exists
        CodingQuestion question = codingQuestionRepository.findByIdAndNotDeleted(createDto.getQuestionId())
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with ID: " + createDto.getQuestionId()));
        
        // Validate company label exists
        Label companyLabel = labelRepository.findByIdAndNotDeleted(createDto.getCompanyLabelId())
                .orElseThrow(() -> new ResourceNotFoundException("Company label not found with ID: " + createDto.getCompanyLabelId()));
        
        // Check if relationship already exists
        if (questionCompanyRepository.existsByQuestionIdAndCompanyLabelId(createDto.getQuestionId(), createDto.getCompanyLabelId())) {
            throw new DuplicateResourceException("Question-company relationship already exists");
        }
        
        QuestionCompany questionCompany = new QuestionCompany(question, companyLabel);
        questionCompany.setFrequencyScore(createDto.getFrequencyScore());
        if (createDto.getInterviewRound() != null && !createDto.getInterviewRound().trim().isEmpty()) {
            questionCompany.setInterviewRound(QuestionCompany.InterviewRound.valueOf(createDto.getInterviewRound()));
        }
        questionCompany.setLastAskedYear(createDto.getLastAskedYear());
        questionCompany.setLastAskedDate(createDto.getLastAskedDate());
        questionCompany.setNotes(createDto.getNotes());
        questionCompany.setIsVerified(createDto.getIsVerified());
        
        questionCompanyRepository.save(questionCompany);
        log.info("Successfully added company to question");
    }

    @Override
    public void removeCompanyFromQuestion(String questionId, String companyLabelId) {
        log.info("Removing company {} from question {}", companyLabelId, questionId);
        
        QuestionCompany questionCompany = questionCompanyRepository.findByQuestionIdAndCompanyLabelId(questionId, companyLabelId)
                .orElseThrow(() -> new ResourceNotFoundException("Question-company relationship not found"));
        
        questionCompany.setDeleted(true);
        questionCompanyRepository.save(questionCompany);
        log.info("Successfully removed company from question");
    }

    // Helper methods for conversion
    private CodingQuestionDto convertToDto(CodingQuestion question) {
        CodingQuestionDto dto = new CodingQuestionDto();
        dto.setId(question.getId());
        dto.setTitle(question.getTitle());
        dto.setShortDescription(question.getShortDescription());
        dto.setFilePath(question.getFilePath());
        
        if (question.getDifficultyLabel() != null) {
            LabelSummaryDto difficultyDto = new LabelSummaryDto();
            difficultyDto.setId(question.getDifficultyLabel().getId());
            difficultyDto.setName(question.getDifficultyLabel().getName());
            dto.setDifficultyLabel(difficultyDto);
        }
        
        if (question.getSource() != null) {
            SourceSummaryDto sourceDto = new SourceSummaryDto();
            sourceDto.setId(question.getSource().getId());
            sourceDto.setCode(question.getSource().getCode());
            sourceDto.setName(question.getSource().getName());
            sourceDto.setBaseUrl(question.getSource().getBaseUrl());
            sourceDto.setColorCode(question.getSource().getColorCode());
            dto.setSource(sourceDto);
        }
        
        dto.setPlatformQuestionId(question.getPlatformQuestionId());
        dto.setOriginalUrl(question.getOriginalUrl());
        dto.setStatus(question.getStatus().toString());
        dto.setCreatedAt(question.getCreatedAt());
        dto.setUpdatedAt(question.getUpdatedAt());
        dto.setVersion(question.getVersion());

        return dto;
    }

    private CodingQuestionSummaryDto convertToSummaryDto(CodingQuestion question) {
        CodingQuestionSummaryDto dto = new CodingQuestionSummaryDto();
        dto.setId(question.getId());
        dto.setTitle(question.getTitle());
        dto.setShortDescription(question.getShortDescription());
        
        if (question.getDifficultyLabel() != null) {
            LabelSummaryDto difficultyDto = new LabelSummaryDto();
            difficultyDto.setId(question.getDifficultyLabel().getId());
            difficultyDto.setName(question.getDifficultyLabel().getName());
            dto.setDifficultyLabel(difficultyDto);
        }
        
        if (question.getSource() != null) {
            dto.setSourceName(question.getSource().getName());
        }
        dto.setStatus(question.getStatus().toString());
        dto.setCreatedAt(question.getCreatedAt());

        return dto;
    }
}