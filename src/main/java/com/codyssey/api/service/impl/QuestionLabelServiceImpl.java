package com.codyssey.api.service.impl;

import com.codyssey.api.dto.question.QuestionLabelCreateDto;
import com.codyssey.api.dto.question.QuestionLabelBulkCreateDto;
import com.codyssey.api.dto.label.LabelSummaryDto;
import com.codyssey.api.exception.DuplicateResourceException;
import com.codyssey.api.exception.ResourceNotFoundException;
import com.codyssey.api.model.*;
import com.codyssey.api.repository.*;
import com.codyssey.api.service.QuestionLabelService;
import com.codyssey.api.dto.question.LabelUsageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of QuestionLabelService
 * <p>
 * Provides question-label association management functionality including
 * bulk operations, label replacement, and usage analytics.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class QuestionLabelServiceImpl implements QuestionLabelService {

    private final QuestionLabelRepository questionLabelRepository;
    private final CodingQuestionRepository questionRepository;
    private final LabelRepository labelRepository;

    @Override
    public String createQuestionLabel(QuestionLabelCreateDto createDto) {
        log.info("Creating question-label association: question={}, label={}", 
                createDto.getQuestionId(), createDto.getLabelId());

        // Validate question exists
        CodingQuestion question = questionRepository.findByIdAndNotDeleted(createDto.getQuestionId())
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with ID: " + createDto.getQuestionId()));

        // Validate label exists
        Label label = labelRepository.findByIdAndNotDeleted(createDto.getLabelId())
                .orElseThrow(() -> new ResourceNotFoundException("Label not found with ID: " + createDto.getLabelId()));

        // Check if association already exists
        if (questionLabelRepository.existsByQuestionIdAndLabelId(createDto.getQuestionId(), createDto.getLabelId())) {
            throw new DuplicateResourceException("Question-label association already exists");
        }

        // Create the association
        QuestionLabel questionLabel = new QuestionLabel(question, label);
        questionLabelRepository.save(questionLabel);

        log.info("Successfully created question-label association with ID: {}", questionLabel.getId());
        return "Association created successfully between question " + createDto.getQuestionId() + 
               " and label " + createDto.getLabelId();
    }

    @Override
    public List<String> createQuestionLabelsBulk(QuestionLabelBulkCreateDto bulkCreateDto) {
        log.info("Creating {} question-label associations in bulk", bulkCreateDto.getQuestionLabels().size());

        List<String> results = new ArrayList<>();

        for (QuestionLabelCreateDto createDto : bulkCreateDto.getQuestionLabels()) {
            try {
                String result = createQuestionLabel(createDto);
                results.add(result);
            } catch (Exception e) {
                log.error("Failed to create association for question: {}, label: {} - {}", 
                        createDto.getQuestionId(), createDto.getLabelId(), e.getMessage());
                results.add("Failed: " + e.getMessage());
            }
        }

        log.info("Completed bulk creation of question-label associations");
        return results;
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabelSummaryDto> getLabelsByQuestionId(String questionId) {
        log.info("Retrieving labels for question ID: {}", questionId);
        List<Label> labels = questionLabelRepository.findLabelsByQuestionId(questionId);
        return labels.stream()
                .map(this::convertToLabelSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getQuestionIdsByLabelId(String labelId) {
        log.info("Retrieving question IDs for label ID: {}", labelId);
        List<CodingQuestion> questions = questionLabelRepository.findQuestionsByLabelId(labelId);
        return questions.stream()
                .map(CodingQuestion::getId)
                .collect(Collectors.toList());
    }

    @Override
    public void removeQuestionLabel(String questionId, String labelId) {
        log.info("Removing label {} from question {}", labelId, questionId);

        QuestionLabel questionLabel = questionLabelRepository.findByQuestionIdAndLabelId(questionId, labelId)
                .orElseThrow(() -> new ResourceNotFoundException("Question-label association not found"));

        questionLabelRepository.delete(questionLabel);
        log.info("Successfully removed label {} from question {}", labelId, questionId);
    }

    @Override
    public void removeAllLabelsFromQuestion(String questionId) {
        log.info("Removing all labels from question: {}", questionId);

        // Validate question exists
        if (!questionRepository.existsById(questionId)) {
            throw new ResourceNotFoundException("Question not found with ID: " + questionId);
        }

        questionLabelRepository.deleteByQuestionId(questionId);
        log.info("Successfully removed all labels from question: {}", questionId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isQuestionLabelExists(String questionId, String labelId) {
        log.info("Checking if question-label association exists: question={}, label={}", questionId, labelId);
        return questionLabelRepository.existsByQuestionIdAndLabelId(questionId, labelId);
    }

    @Override
    public List<LabelSummaryDto> replaceQuestionLabels(String questionId, List<String> labelIds) {
        log.info("Replacing all labels for question {} with {} new labels", questionId, labelIds.size());

        // Validate question exists
        CodingQuestion question = questionRepository.findByIdAndNotDeleted(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with ID: " + questionId));

        // Validate all labels exist
        List<Label> labels = new ArrayList<>();
        for (String labelId : labelIds) {
            Label label = labelRepository.findByIdAndNotDeleted(labelId)
                    .orElseThrow(() -> new ResourceNotFoundException("Label not found with ID: " + labelId));
            labels.add(label);
        }

        // Remove all existing associations
        questionLabelRepository.deleteByQuestionId(questionId);

        // Create new associations
        for (Label label : labels) {
            QuestionLabel questionLabel = new QuestionLabel(question, label);
            questionLabelRepository.save(questionLabel);
        }

        log.info("Successfully replaced labels for question: {}", questionId);
        return labels.stream()
                .map(this::convertToLabelSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<LabelSummaryDto> addLabelsToQuestion(String questionId, List<String> labelIds) {
        log.info("Adding {} labels to question {}", labelIds.size(), questionId);

        // Validate question exists
        CodingQuestion question = questionRepository.findByIdAndNotDeleted(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with ID: " + questionId));

        // Add new labels (skip if already exists)
        for (String labelId : labelIds) {
            if (!questionLabelRepository.existsByQuestionIdAndLabelId(questionId, labelId)) {
                Label label = labelRepository.findByIdAndNotDeleted(labelId)
                        .orElseThrow(() -> new ResourceNotFoundException("Label not found with ID: " + labelId));
                
                QuestionLabel questionLabel = new QuestionLabel(question, label);
                questionLabelRepository.save(questionLabel);
            }
        }

        log.info("Successfully added labels to question: {}", questionId);
        return getLabelsByQuestionId(questionId);
    }

    @Override
    public List<LabelSummaryDto> removeLabelsFromQuestion(String questionId, List<String> labelIds) {
        log.info("Removing {} labels from question {}", labelIds.size(), questionId);

        for (String labelId : labelIds) {
            Optional<QuestionLabel> questionLabel = questionLabelRepository.findByQuestionIdAndLabelId(questionId, labelId);
            if (questionLabel.isPresent()) {
                questionLabelRepository.delete(questionLabel.get());
            }
        }

        log.info("Successfully removed labels from question: {}", questionId);
        return getLabelsByQuestionId(questionId);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getLabelsCountByQuestionId(String questionId) {
        log.info("Getting labels count for question: {}", questionId);
        return questionLabelRepository.countByQuestionId(questionId);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getQuestionsCountByLabelId(String labelId) {
        log.info("Getting questions count for label: {}", labelId);
        return questionLabelRepository.countByLabelId(labelId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LabelUsageDto> getMostUsedLabels(int limit) {
        log.info("Getting {} most used labels", limit);
        
        // This would require a custom query to group by label and count usage
        // For now, returning empty list as this requires more complex query implementation
        // TODO: Implement custom query for label usage statistics
        return new ArrayList<>();
    }

    // Helper method for conversion
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