package com.codyssey.api.service.impl;

import com.codyssey.api.dto.question.*;
import com.codyssey.api.dto.label.LabelSummaryDto;
import com.codyssey.api.exception.DuplicateResourceException;
import com.codyssey.api.exception.ResourceNotFoundException;
import com.codyssey.api.model.*;
import com.codyssey.api.repository.*;
import com.codyssey.api.service.CodingQuestionService;
import com.codyssey.api.dto.question.QuestionStatisticsDto;
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
    private final QuestionSolutionRepository solutionRepository;
    private final QuestionLabelRepository questionLabelRepository;
    private final QuestionCompanyRepository questionCompanyRepository;
    private final QuestionLinkRepository questionLinkRepository;
    private final QuestionTestCaseRepository testCaseRepository;
    private final QuestionMediaRepository mediaRepository;

    @Override
    public CodingQuestionDto createQuestion(CodingQuestionCreateDto createDto) {
        log.info("Creating new coding question with title: {}", createDto.getTitle());

        // Validate difficulty label if provided
        Label difficultyLabel = null;
        if (createDto.getDifficultyLabelId() != null && !createDto.getDifficultyLabelId().trim().isEmpty()) {
            difficultyLabel = labelRepository.findByIdAndNotDeleted(createDto.getDifficultyLabelId())
                    .orElseThrow(() -> new ResourceNotFoundException("Difficulty label not found with ID: " + createDto.getDifficultyLabelId()));
        }

        // Validate user if provided
        User createdByUser = null;
        if (createDto.getCreatedByUserId() != null && !createDto.getCreatedByUserId().trim().isEmpty()) {
            createdByUser = userRepository.findByIdAndNotDeleted(createDto.getCreatedByUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + createDto.getCreatedByUserId()));
        }

        // Check for duplicate title within platform
        if (createDto.getPlatformSource() != null && !createDto.getPlatformSource().trim().isEmpty()) {
            if (codingQuestionRepository.existsByTitleAndPlatformSource(createDto.getTitle(), createDto.getPlatformSource())) {
                throw new DuplicateResourceException("Question with title '" + createDto.getTitle() + 
                        "' already exists for platform: " + createDto.getPlatformSource());
            }
        }

        // Create the question entity
        CodingQuestion question = new CodingQuestion();
        question.setTitle(createDto.getTitle());
        question.setShortDescription(createDto.getShortDescription());
        question.setDescription(createDto.getDescription());
        question.setDifficultyLabel(difficultyLabel);
        question.setPlatformSource(createDto.getPlatformSource());
        question.setPlatformQuestionId(createDto.getPlatformQuestionId());
        question.setInputFormat(createDto.getInputFormat());
        question.setOutputFormat(createDto.getOutputFormat());
        question.setConstraintsText(createDto.getConstraintsText());
        question.setTimeComplexityHint(createDto.getTimeComplexityHint());
        question.setSpaceComplexityHint(createDto.getSpaceComplexityHint());
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
    public Page<CodingQuestionSummaryDto> getAllQuestions(Pageable pageable) {
        log.info("Retrieving coding questions with pagination: {}", pageable);
        Page<CodingQuestion> questions = codingQuestionRepository.findAll(pageable);
        return questions.map(this::convertToSummaryDto);
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
        if (updateDto.getDescription() != null) {
            question.setDescription(updateDto.getDescription());
        }
        if (updateDto.getDifficultyLabelId() != null) {
            Label difficultyLabel = labelRepository.findByIdAndNotDeleted(updateDto.getDifficultyLabelId())
                    .orElseThrow(() -> new ResourceNotFoundException("Difficulty label not found with ID: " + updateDto.getDifficultyLabelId()));
            question.setDifficultyLabel(difficultyLabel);
        }
        if (updateDto.getPlatformSource() != null) {
            question.setPlatformSource(updateDto.getPlatformSource());
        }
        if (updateDto.getPlatformQuestionId() != null) {
            question.setPlatformQuestionId(updateDto.getPlatformQuestionId());
        }
        if (updateDto.getInputFormat() != null) {
            question.setInputFormat(updateDto.getInputFormat());
        }
        if (updateDto.getOutputFormat() != null) {
            question.setOutputFormat(updateDto.getOutputFormat());
        }
        if (updateDto.getConstraintsText() != null) {
            question.setConstraintsText(updateDto.getConstraintsText());
        }
        if (updateDto.getTimeComplexityHint() != null) {
            question.setTimeComplexityHint(updateDto.getTimeComplexityHint());
        }
        if (updateDto.getSpaceComplexityHint() != null) {
            question.setSpaceComplexityHint(updateDto.getSpaceComplexityHint());
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
        log.info("Soft deleting coding question with ID: {}", id);

        CodingQuestion question = codingQuestionRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coding question not found with ID: " + id));

        question.setDeleted(true);
        codingQuestionRepository.save(question);

        log.info("Successfully soft deleted coding question with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CodingQuestionSummaryDto> getQuestionsByDifficulty(String difficultyLabelId) {
        log.info("Retrieving questions by difficulty label ID: {}", difficultyLabelId);
        
        Label difficultyLabel = labelRepository.findByIdAndNotDeleted(difficultyLabelId)
                .orElseThrow(() -> new ResourceNotFoundException("Difficulty label not found with ID: " + difficultyLabelId));

        List<CodingQuestion> questions = codingQuestionRepository.findByDifficultyLabel(difficultyLabel);
        return questions.stream()
                .map(this::convertToSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CodingQuestionSummaryDto> getQuestionsByPlatform(String platformSource) {
        log.info("Retrieving questions by platform: {}", platformSource);
        List<CodingQuestion> questions = codingQuestionRepository.findByPlatformSource(platformSource);
        return questions.stream()
                .map(this::convertToSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CodingQuestionSummaryDto> searchQuestions(String searchTerm, Pageable pageable) {
        log.info("Searching questions with term: {} and pagination: {}", searchTerm, pageable);
        Page<CodingQuestion> questions = codingQuestionRepository.searchByTitleOrDescription(searchTerm, pageable);
        return questions.map(this::convertToSummaryDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CodingQuestionSummaryDto> getQuestionsByLabel(String labelId) {
        log.info("Retrieving questions by label ID: {}", labelId);
        List<CodingQuestion> questions = codingQuestionRepository.findByLabelId(labelId);
        return questions.stream()
                .map(this::convertToSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CodingQuestionSummaryDto> getQuestionsByCompany(String companyLabelId) {
        log.info("Retrieving questions by company label ID: {}", companyLabelId);
        List<CodingQuestion> questions = codingQuestionRepository.findByCompanyLabelId(companyLabelId);
        return questions.stream()
                .map(this::convertToSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CodingQuestionSummaryDto> getQuestionsByUser(String userId) {
        log.info("Retrieving questions by user ID: {}", userId);
        List<CodingQuestion> questions = codingQuestionRepository.findByCreatedByUserId(userId);
        return questions.stream()
                .map(this::convertToSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CodingQuestionSummaryDto> getActiveQuestions() {
        log.info("Retrieving active questions");
        List<CodingQuestion> questions = codingQuestionRepository.findActiveQuestions();
        return questions.stream()
                .map(this::convertToSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CodingQuestionSummaryDto> getQuestionsByStatus(String status, Pageable pageable) {
        log.info("Retrieving questions by status: {} with pagination: {}", status, pageable);
        CodingQuestion.QuestionStatus questionStatus = CodingQuestion.QuestionStatus.valueOf(status);
        Page<CodingQuestion> questions = codingQuestionRepository.findByStatus(questionStatus, pageable);
        return questions.map(this::convertToSummaryDto);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkTitleAvailability(String title, String platformSource) {
        log.info("Checking title availability: {} for platform: {}", title, platformSource);
        return !codingQuestionRepository.existsByTitleAndPlatformSource(title, platformSource);
    }

    @Override
    @Transactional(readOnly = true)
    public QuestionStatisticsDto getQuestionStatistics(String questionId) {
        log.info("Retrieving statistics for question ID: {}", questionId);

        if (!codingQuestionRepository.existsById(questionId)) {
            throw new ResourceNotFoundException("Coding question not found with ID: " + questionId);
        }

        Long solutionsCount = solutionRepository.countByQuestionId(questionId);
        Long testCasesCount = testCaseRepository.countByQuestionId(questionId);
        Long mediaFilesCount = mediaRepository.countByQuestionId(questionId);
        Long labelsCount = questionLabelRepository.countByQuestionId(questionId);
        Long companiesCount = questionCompanyRepository.countByQuestionId(questionId);
        Long outgoingLinksCount = questionLinkRepository.countBySourceQuestionId(questionId);
        Long incomingLinksCount = questionLinkRepository.countByTargetQuestionId(questionId);

        return new QuestionStatisticsDto(questionId, solutionsCount, testCasesCount, 
                mediaFilesCount, labelsCount, companiesCount, outgoingLinksCount, incomingLinksCount);
    }

    // Helper methods for conversion
    private CodingQuestionDto convertToDto(CodingQuestion question) {
        CodingQuestionDto dto = new CodingQuestionDto();
        dto.setId(question.getId());
        dto.setTitle(question.getTitle());
        dto.setShortDescription(question.getShortDescription());
        dto.setDescription(question.getDescription());
        
        if (question.getDifficultyLabel() != null) {
            LabelSummaryDto difficultyDto = new LabelSummaryDto();
            difficultyDto.setId(question.getDifficultyLabel().getId());
            difficultyDto.setName(question.getDifficultyLabel().getName());
            dto.setDifficultyLabel(difficultyDto);
        }
        
        dto.setPlatformSource(question.getPlatformSource());
        dto.setPlatformQuestionId(question.getPlatformQuestionId());
        dto.setInputFormat(question.getInputFormat());
        dto.setOutputFormat(question.getOutputFormat());
        dto.setConstraintsText(question.getConstraintsText());
        dto.setTimeComplexityHint(question.getTimeComplexityHint());
        dto.setSpaceComplexityHint(question.getSpaceComplexityHint());
        dto.setStatus(question.getStatus().toString());
        dto.setCreatedAt(question.getCreatedAt());
        dto.setUpdatedAt(question.getUpdatedAt());
        dto.setVersion(question.getVersion());

        // Add counts
        dto.setSolutionsCount(solutionRepository.countByQuestionId(question.getId()).intValue());
        dto.setTestCasesCount(testCaseRepository.countByQuestionId(question.getId()).intValue());
        dto.setMediaFilesCount(mediaRepository.countByQuestionId(question.getId()).intValue());

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
        
        dto.setPlatformSource(question.getPlatformSource());
        dto.setStatus(question.getStatus().toString());
        dto.setCreatedAt(question.getCreatedAt());
        dto.setSolutionsCount(solutionRepository.countByQuestionId(question.getId()).intValue());

        return dto;
    }
}