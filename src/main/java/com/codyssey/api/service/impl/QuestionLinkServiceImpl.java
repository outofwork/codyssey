package com.codyssey.api.service.impl;

import com.codyssey.api.dto.link.QuestionLinkCreateDto;
import com.codyssey.api.dto.link.QuestionLinkBulkCreateDto;
import com.codyssey.api.dto.question.CodingQuestionSummaryDto;
import com.codyssey.api.dto.label.LabelSummaryDto;
import com.codyssey.api.exception.DuplicateResourceException;
import com.codyssey.api.exception.ResourceNotFoundException;
import com.codyssey.api.model.*;
import com.codyssey.api.repository.*;
import com.codyssey.api.service.QuestionLinkService;
import com.codyssey.api.dto.link.QuestionLinkDto;
import com.codyssey.api.dto.link.LinkSequenceDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of QuestionLinkService
 * <p>
 * Provides question link management functionality including creation, retrieval,
 * deletion, and bulk operations for managing question relationships.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class QuestionLinkServiceImpl implements QuestionLinkService {

    private final QuestionLinkRepository questionLinkRepository;
    private final CodingQuestionRepository questionRepository;

    @Override
    public QuestionLinkDto createQuestionLink(QuestionLinkCreateDto createDto) {
        log.info("Creating question link: source={}, target={}, type={}", 
                createDto.getSourceQuestionId(), createDto.getTargetQuestionId(), createDto.getRelationshipType());

        // Validate source question exists
        CodingQuestion sourceQuestion = questionRepository.findByIdAndNotDeleted(createDto.getSourceQuestionId())
                .orElseThrow(() -> new ResourceNotFoundException("Source question not found with ID: " + createDto.getSourceQuestionId()));

        // Validate target question exists
        CodingQuestion targetQuestion = questionRepository.findByIdAndNotDeleted(createDto.getTargetQuestionId())
                .orElseThrow(() -> new ResourceNotFoundException("Target question not found with ID: " + createDto.getTargetQuestionId()));

        // Validate relationship type
        QuestionLink.RelationshipType relationshipType;
        try {
            relationshipType = QuestionLink.RelationshipType.valueOf(createDto.getRelationshipType());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid relationship type: " + createDto.getRelationshipType());
        }

        // Check if link already exists
        if (questionLinkRepository.existsBySourceQuestionIdAndTargetQuestionIdAndRelationshipType(
                createDto.getSourceQuestionId(), createDto.getTargetQuestionId(), relationshipType)) {
            throw new DuplicateResourceException("Question link already exists with this relationship type");
        }

        // Check for sequence conflicts
        if (questionLinkRepository.existsBySourceQuestionIdAndSequence(createDto.getSourceQuestionId(), createDto.getSequence())) {
            throw new DuplicateResourceException("Link with sequence " + createDto.getSequence() + 
                    " already exists for source question: " + createDto.getSourceQuestionId());
        }

        // Prevent self-linking
        if (createDto.getSourceQuestionId().equals(createDto.getTargetQuestionId())) {
            throw new IllegalArgumentException("Cannot create link from question to itself");
        }

        // Create the link entity
        QuestionLink questionLink = new QuestionLink(sourceQuestion, targetQuestion, 
                createDto.getSequence(), relationshipType);
        QuestionLink savedLink = questionLinkRepository.save(questionLink);

        log.info("Successfully created question link with ID: {}", savedLink.getId());
        return convertToDto(savedLink);
    }

    @Override
    public List<QuestionLinkDto> createQuestionLinksBulk(QuestionLinkBulkCreateDto bulkCreateDto) {
        log.info("Creating {} question links in bulk", bulkCreateDto.getQuestionLinks().size());

        List<QuestionLinkDto> createdLinks = new ArrayList<>();

        for (QuestionLinkCreateDto createDto : bulkCreateDto.getQuestionLinks()) {
            try {
                QuestionLinkDto created = createQuestionLink(createDto);
                createdLinks.add(created);
            } catch (Exception e) {
                log.error("Failed to create link for source: {}, target: {} - {}", 
                        createDto.getSourceQuestionId(), createDto.getTargetQuestionId(), e.getMessage());
                // Continue with other links but log the error
            }
        }

        log.info("Successfully created {}/{} question links in bulk", 
                createdLinks.size(), bulkCreateDto.getQuestionLinks().size());
        return createdLinks;
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionLinkDto> getOutgoingLinksByQuestionId(String sourceQuestionId) {
        log.info("Retrieving outgoing links for question ID: {}", sourceQuestionId);
        List<QuestionLink> links = questionLinkRepository.findBySourceQuestionIdOrderBySequence(sourceQuestionId);
        return links.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionLinkDto> getIncomingLinksByQuestionId(String targetQuestionId) {
        log.info("Retrieving incoming links for question ID: {}", targetQuestionId);
        List<QuestionLink> links = questionLinkRepository.findByTargetQuestionId(targetQuestionId);
        return links.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<QuestionLinkDto> getLinkById(String id) {
        log.info("Retrieving question link by ID: {}", id);
        return questionLinkRepository.findById(id)
                .map(this::convertToDto);
    }

    @Override
    public void deleteQuestionLink(String id) {
        log.info("Deleting question link with ID: {}", id);

        QuestionLink questionLink = questionLinkRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question link not found with ID: " + id));

        questionLinkRepository.delete(questionLink);
        log.info("Successfully deleted question link with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CodingQuestionSummaryDto> getFollowUpQuestions(String sourceQuestionId) {
        log.info("Retrieving follow-up questions for question ID: {}", sourceQuestionId);
        List<CodingQuestion> questions = questionLinkRepository.findFollowUpQuestions(sourceQuestionId);
        return questions.stream()
                .map(this::convertToQuestionSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CodingQuestionSummaryDto> getSimilarQuestions(String sourceQuestionId) {
        log.info("Retrieving similar questions for question ID: {}", sourceQuestionId);
        List<CodingQuestion> questions = questionLinkRepository.findSimilarQuestions(sourceQuestionId);
        return questions.stream()
                .map(this::convertToQuestionSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CodingQuestionSummaryDto> getPrerequisiteQuestions(String sourceQuestionId) {
        log.info("Retrieving prerequisite questions for question ID: {}", sourceQuestionId);
        List<CodingQuestion> questions = questionLinkRepository.findPrerequisiteQuestions(sourceQuestionId);
        return questions.stream()
                .map(this::convertToQuestionSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionLinkDto> getLinksByRelationshipType(String sourceQuestionId, String relationshipType) {
        log.info("Retrieving links for question {} with relationship type: {}", sourceQuestionId, relationshipType);
        
        QuestionLink.RelationshipType type;
        try {
            type = QuestionLink.RelationshipType.valueOf(relationshipType);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid relationship type: " + relationshipType);
        }

        List<QuestionLink> links = questionLinkRepository.findBySourceQuestionIdAndRelationshipType(sourceQuestionId, type);
        return links.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkSequenceAvailability(String sourceQuestionId, Integer sequence) {
        log.info("Checking sequence availability: {} for source question: {}", sequence, sourceQuestionId);
        return !questionLinkRepository.existsBySourceQuestionIdAndSequence(sourceQuestionId, sequence);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getNextSequenceNumber(String sourceQuestionId) {
        log.info("Getting next sequence number for source question: {}", sourceQuestionId);
        Integer maxSequence = questionLinkRepository.findMaxSequenceBySourceQuestionId(sourceQuestionId);
        return maxSequence + 1;
    }

    @Override
    public void reorderLinks(String sourceQuestionId, List<LinkSequenceDto> linkSequences) {
        log.info("Reordering {} links for source question: {}", linkSequences.size(), sourceQuestionId);

        // Validate source question exists
        if (!questionRepository.existsById(sourceQuestionId)) {
            throw new ResourceNotFoundException("Source question not found with ID: " + sourceQuestionId);
        }

        for (LinkSequenceDto sequenceDto : linkSequences) {
            QuestionLink link = questionLinkRepository.findById(sequenceDto.getLinkId())
                    .orElseThrow(() -> new ResourceNotFoundException("Question link not found with ID: " + sequenceDto.getLinkId()));

            // Ensure link belongs to the specified source question
            if (!link.getSourceQuestion().getId().equals(sourceQuestionId)) {
                throw new IllegalArgumentException("Link " + sequenceDto.getLinkId() + 
                        " does not belong to source question " + sourceQuestionId);
            }

            link.setSequence(sequenceDto.getSequence());
            questionLinkRepository.save(link);
        }

        log.info("Successfully reordered links for source question: {}", sourceQuestionId);
    }

    @Override
    public void removeAllLinksForQuestion(String questionId) {
        log.info("Removing all links for question: {}", questionId);

        // Validate question exists
        if (!questionRepository.existsById(questionId)) {
            throw new ResourceNotFoundException("Question not found with ID: " + questionId);
        }

        questionLinkRepository.deleteByQuestionId(questionId);
        log.info("Successfully removed all links for question: {}", questionId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isLinkExists(String sourceQuestionId, String targetQuestionId, String relationshipType) {
        log.info("Checking if link exists: source={}, target={}, type={}", sourceQuestionId, targetQuestionId, relationshipType);
        
        QuestionLink.RelationshipType type;
        try {
            type = QuestionLink.RelationshipType.valueOf(relationshipType);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid relationship type: " + relationshipType);
        }

        return questionLinkRepository.existsBySourceQuestionIdAndTargetQuestionIdAndRelationshipType(
                sourceQuestionId, targetQuestionId, type);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getOutgoingLinksCount(String sourceQuestionId) {
        log.info("Getting outgoing links count for question: {}", sourceQuestionId);
        return questionLinkRepository.countBySourceQuestionId(sourceQuestionId);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getIncomingLinksCount(String targetQuestionId) {
        log.info("Getting incoming links count for question: {}", targetQuestionId);
        return questionLinkRepository.countByTargetQuestionId(targetQuestionId);
    }

    // Helper methods for conversion
    private QuestionLinkDto convertToDto(QuestionLink link) {
        QuestionLinkDto dto = new QuestionLinkDto();
        dto.setId(link.getId());
        dto.setSourceQuestionId(link.getSourceQuestion().getId());
        dto.setSourceQuestionTitle(link.getSourceQuestion().getTitle());
        dto.setTargetQuestionId(link.getTargetQuestion().getId());
        dto.setTargetQuestionTitle(link.getTargetQuestion().getTitle());
        dto.setSequence(link.getSequence());
        dto.setRelationshipType(link.getRelationshipType().toString());
        return dto;
    }

    private CodingQuestionSummaryDto convertToQuestionSummaryDto(CodingQuestion question) {
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
        
        return dto;
    }
}