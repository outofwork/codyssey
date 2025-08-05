package com.codyssey.api.service.impl;

import com.codyssey.api.dto.solution.*;
import com.codyssey.api.dto.user.UserDto;
import com.codyssey.api.exception.DuplicateResourceException;
import com.codyssey.api.exception.ResourceNotFoundException;
import com.codyssey.api.model.*;
import com.codyssey.api.repository.*;
import com.codyssey.api.service.QuestionSolutionService;
import com.codyssey.api.dto.solution.SolutionSequenceDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of QuestionSolutionService
 * <p>
 * Provides solution management functionality including creation, retrieval,
 * updating, deletion, and bulk operations with sequence management.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class QuestionSolutionServiceImpl implements QuestionSolutionService {

    private final QuestionSolutionRepository solutionRepository;
    private final CodingQuestionRepository questionRepository;
    private final UserRepository userRepository;

    @Override
    public QuestionSolutionDto createSolution(QuestionSolutionCreateDto createDto) {
        log.info("Creating new solution for question: {} in language: {}", createDto.getQuestionId(), createDto.getLanguage());

        // Validate question exists
        CodingQuestion question = questionRepository.findByIdAndNotDeleted(createDto.getQuestionId())
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with ID: " + createDto.getQuestionId()));

        // Validate user if provided
        User createdByUser = null;
        if (createDto.getCreatedByUserId() != null && !createDto.getCreatedByUserId().trim().isEmpty()) {
            createdByUser = userRepository.findByIdAndNotDeleted(createDto.getCreatedByUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + createDto.getCreatedByUserId()));
        }

        // Check for sequence conflicts
        if (solutionRepository.existsByQuestionIdAndSequence(createDto.getQuestionId(), createDto.getSequence())) {
            throw new DuplicateResourceException("Solution with sequence " + createDto.getSequence() + 
                    " already exists for question: " + createDto.getQuestionId());
        }

        // Create the solution entity
        QuestionSolution solution = new QuestionSolution();
        solution.setQuestion(question);
        solution.setSequence(createDto.getSequence());
        solution.setLanguage(createDto.getLanguage());
        solution.setSolutionCode(createDto.getSolutionCode());
        solution.setExplanation(createDto.getExplanation());
        solution.setTimeComplexity(createDto.getTimeComplexity());
        solution.setSpaceComplexity(createDto.getSpaceComplexity());
        solution.setIsOptimal(createDto.getIsOptimal() != null ? createDto.getIsOptimal() : false);
        solution.setCreatedByUser(createdByUser);

        QuestionSolution savedSolution = solutionRepository.save(solution);
        log.info("Successfully created solution with ID: {}", savedSolution.getId());

        return convertToDto(savedSolution);
    }

    @Override
    public List<QuestionSolutionDto> createSolutionsBulk(QuestionSolutionBulkCreateDto bulkCreateDto) {
        log.info("Creating {} solutions in bulk", bulkCreateDto.getSolutions().size());

        List<QuestionSolutionDto> createdSolutions = new ArrayList<>();

        for (QuestionSolutionCreateDto createDto : bulkCreateDto.getSolutions()) {
            try {
                QuestionSolutionDto created = createSolution(createDto);
                createdSolutions.add(created);
            } catch (Exception e) {
                log.error("Failed to create solution for question: {} - {}", createDto.getQuestionId(), e.getMessage());
                // Continue with other solutions but log the error
            }
        }

        log.info("Successfully created {}/{} solutions in bulk", 
                createdSolutions.size(), bulkCreateDto.getSolutions().size());
        return createdSolutions;
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionSolutionDto> getAllSolutions() {
        log.info("Retrieving all solutions");
        List<QuestionSolution> solutions = solutionRepository.findByDeletedFalse();
        return solutions.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<QuestionSolutionDto> getSolutionById(String id) {
        log.info("Retrieving solution by ID: {}", id);
        return solutionRepository.findByIdAndNotDeleted(id)
                .map(this::convertToDto);
    }

    @Override
    public QuestionSolutionDto updateSolution(String id, QuestionSolutionUpdateDto updateDto) {
        log.info("Updating solution with ID: {}", id);

        QuestionSolution solution = solutionRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException("Solution not found with ID: " + id));

        // Update fields if provided
        if (updateDto.getSequence() != null) {
            // Check for sequence conflicts (excluding current solution)
            if (solutionRepository.existsByQuestionIdAndSequenceExcludingId(
                    solution.getQuestion().getId(), updateDto.getSequence(), id)) {
                throw new DuplicateResourceException("Solution with sequence " + updateDto.getSequence() + 
                        " already exists for this question");
            }
            solution.setSequence(updateDto.getSequence());
        }
        if (updateDto.getLanguage() != null) {
            solution.setLanguage(updateDto.getLanguage());
        }
        if (updateDto.getSolutionCode() != null) {
            solution.setSolutionCode(updateDto.getSolutionCode());
        }
        if (updateDto.getExplanation() != null) {
            solution.setExplanation(updateDto.getExplanation());
        }
        if (updateDto.getTimeComplexity() != null) {
            solution.setTimeComplexity(updateDto.getTimeComplexity());
        }
        if (updateDto.getSpaceComplexity() != null) {
            solution.setSpaceComplexity(updateDto.getSpaceComplexity());
        }
        if (updateDto.getIsOptimal() != null) {
            solution.setIsOptimal(updateDto.getIsOptimal());
        }

        QuestionSolution savedSolution = solutionRepository.save(solution);
        log.info("Successfully updated solution with ID: {}", savedSolution.getId());

        return convertToDto(savedSolution);
    }

    @Override
    public void deleteSolution(String id) {
        log.info("Soft deleting solution with ID: {}", id);

        QuestionSolution solution = solutionRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException("Solution not found with ID: " + id));

        solution.setDeleted(true);
        solutionRepository.save(solution);

        log.info("Successfully soft deleted solution with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionSolutionDto> getSolutionsByQuestionId(String questionId) {
        log.info("Retrieving solutions for question ID: {}", questionId);
        List<QuestionSolution> solutions = solutionRepository.findByQuestionIdOrderBySequence(questionId);
        return solutions.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionSolutionDto> getSolutionsByLanguage(String language) {
        log.info("Retrieving solutions by language: {}", language);
        List<QuestionSolution> solutions = solutionRepository.findByLanguage(language);
        return solutions.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionSolutionDto> getOptimalSolutionsByQuestionId(String questionId) {
        log.info("Retrieving optimal solutions for question ID: {}", questionId);
        List<QuestionSolution> solutions = solutionRepository.findOptimalSolutionsByQuestionId(questionId);
        return solutions.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionSolutionDto> getSolutionsByQuestionIdAndLanguage(String questionId, String language) {
        log.info("Retrieving solutions for question: {} in language: {}", questionId, language);
        List<QuestionSolution> solutions = solutionRepository.findByQuestionIdAndLanguage(questionId, language);
        return solutions.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionSolutionDto> getSolutionsByUser(String userId) {
        log.info("Retrieving solutions by user ID: {}", userId);
        List<QuestionSolution> solutions = solutionRepository.findByCreatedByUserId(userId);
        return solutions.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkSequenceAvailability(String questionId, Integer sequence) {
        log.info("Checking sequence availability: {} for question: {}", sequence, questionId);
        return !solutionRepository.existsByQuestionIdAndSequence(questionId, sequence);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getNextSequenceNumber(String questionId) {
        log.info("Getting next sequence number for question: {}", questionId);
        Integer maxSequence = solutionRepository.findMaxSequenceByQuestionId(questionId);
        return maxSequence + 1;
    }

    @Override
    public void reorderSolutions(String questionId, List<SolutionSequenceDto> solutionSequences) {
        log.info("Reordering {} solutions for question: {}", solutionSequences.size(), questionId);

        // Validate question exists
        if (!questionRepository.existsById(questionId)) {
            throw new ResourceNotFoundException("Question not found with ID: " + questionId);
        }

        for (SolutionSequenceDto sequenceDto : solutionSequences) {
            QuestionSolution solution = solutionRepository.findByIdAndNotDeleted(sequenceDto.getSolutionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Solution not found with ID: " + sequenceDto.getSolutionId()));

            // Ensure solution belongs to the specified question
            if (!solution.getQuestion().getId().equals(questionId)) {
                throw new IllegalArgumentException("Solution " + sequenceDto.getSolutionId() + 
                        " does not belong to question " + questionId);
            }

            solution.setSequence(sequenceDto.getSequence());
            solutionRepository.save(solution);
        }

        log.info("Successfully reordered solutions for question: {}", questionId);
    }

    @Override
    public QuestionSolutionDto markSolutionOptimal(String id, Boolean isOptimal) {
        log.info("Marking solution {} as optimal: {}", id, isOptimal);

        QuestionSolution solution = solutionRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException("Solution not found with ID: " + id));

        solution.setIsOptimal(isOptimal);
        QuestionSolution savedSolution = solutionRepository.save(solution);

        log.info("Successfully marked solution {} as optimal: {}", id, isOptimal);
        return convertToDto(savedSolution);
    }

    // Helper method for conversion
    private QuestionSolutionDto convertToDto(QuestionSolution solution) {
        QuestionSolutionDto dto = new QuestionSolutionDto();
        dto.setId(solution.getId());
        dto.setQuestionId(solution.getQuestion().getId());
        dto.setSequence(solution.getSequence());
        dto.setLanguage(solution.getLanguage());
        dto.setSolutionCode(solution.getSolutionCode());
        dto.setExplanation(solution.getExplanation());
        dto.setTimeComplexity(solution.getTimeComplexity());
        dto.setSpaceComplexity(solution.getSpaceComplexity());
        dto.setIsOptimal(solution.getIsOptimal());
        
        if (solution.getCreatedByUser() != null) {
            UserDto userDto = new UserDto();
            userDto.setId(solution.getCreatedByUser().getId());
            userDto.setUsername(solution.getCreatedByUser().getUsername());
            userDto.setEmail(solution.getCreatedByUser().getEmail());
            dto.setCreatedByUser(userDto);
        }
        
        dto.setCreatedAt(solution.getCreatedAt());
        dto.setUpdatedAt(solution.getUpdatedAt());
        dto.setVersion(solution.getVersion());

        return dto;
    }
}