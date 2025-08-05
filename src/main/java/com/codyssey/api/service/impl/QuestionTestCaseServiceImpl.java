package com.codyssey.api.service.impl;

import com.codyssey.api.dto.testcase.*;
import com.codyssey.api.exception.DuplicateResourceException;
import com.codyssey.api.exception.ResourceNotFoundException;
import com.codyssey.api.model.*;
import com.codyssey.api.repository.*;
import com.codyssey.api.service.QuestionTestCaseService;
import com.codyssey.api.dto.testcase.QuestionTestCaseUpdateDto;
import com.codyssey.api.dto.testcase.TestCaseSequenceDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of QuestionTestCaseService
 * <p>
 * Provides test case management functionality including creation, retrieval,
 * updating, deletion, and bulk operations with sequence management.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class QuestionTestCaseServiceImpl implements QuestionTestCaseService {

    private final QuestionTestCaseRepository testCaseRepository;
    private final CodingQuestionRepository questionRepository;

    @Override
    public QuestionTestCaseDto createTestCase(QuestionTestCaseCreateDto createDto) {
        log.info("Creating new test case for question: {} with sequence: {}", createDto.getQuestionId(), createDto.getSequence());

        // Validate question exists
        CodingQuestion question = questionRepository.findByIdAndNotDeleted(createDto.getQuestionId())
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with ID: " + createDto.getQuestionId()));

        // Check for sequence conflicts
        if (testCaseRepository.existsByQuestionIdAndSequence(createDto.getQuestionId(), createDto.getSequence())) {
            throw new DuplicateResourceException("Test case with sequence " + createDto.getSequence() + 
                    " already exists for question: " + createDto.getQuestionId());
        }

        // Create the test case entity
        QuestionTestCase testCase = new QuestionTestCase();
        testCase.setQuestion(question);
        testCase.setSequence(createDto.getSequence());
        testCase.setInputData(createDto.getInputData());
        testCase.setExpectedOutput(createDto.getExpectedOutput());
        testCase.setIsSample(createDto.getIsSample() != null ? createDto.getIsSample() : false);
        testCase.setExplanation(createDto.getExplanation());

        QuestionTestCase savedTestCase = testCaseRepository.save(testCase);
        log.info("Successfully created test case with ID: {}", savedTestCase.getId());

        return convertToDto(savedTestCase);
    }

    @Override
    public List<QuestionTestCaseDto> createTestCasesBulk(QuestionTestCaseBulkCreateDto bulkCreateDto) {
        log.info("Creating {} test cases in bulk", bulkCreateDto.getTestCases().size());

        List<QuestionTestCaseDto> createdTestCases = new ArrayList<>();

        for (QuestionTestCaseCreateDto createDto : bulkCreateDto.getTestCases()) {
            try {
                QuestionTestCaseDto created = createTestCase(createDto);
                createdTestCases.add(created);
            } catch (Exception e) {
                log.error("Failed to create test case for question: {} - {}", createDto.getQuestionId(), e.getMessage());
                // Continue with other test cases but log the error
            }
        }

        log.info("Successfully created {}/{} test cases in bulk", 
                createdTestCases.size(), bulkCreateDto.getTestCases().size());
        return createdTestCases;
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionTestCaseDto> getAllTestCases() {
        log.info("Retrieving all test cases");
        List<QuestionTestCase> testCases = testCaseRepository.findByDeletedFalse();
        return testCases.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<QuestionTestCaseDto> getTestCaseById(String id) {
        log.info("Retrieving test case by ID: {}", id);
        return testCaseRepository.findByIdAndNotDeleted(id)
                .map(this::convertToDto);
    }

    @Override
    public QuestionTestCaseDto updateTestCase(String id, QuestionTestCaseUpdateDto updateDto) {
        log.info("Updating test case with ID: {}", id);

        QuestionTestCase testCase = testCaseRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException("Test case not found with ID: " + id));

        // Update fields if provided
        if (updateDto.getSequence() != null) {
            // Check for sequence conflicts (excluding current test case)
            if (testCaseRepository.existsByQuestionIdAndSequenceExcludingId(
                    testCase.getQuestion().getId(), updateDto.getSequence(), id)) {
                throw new DuplicateResourceException("Test case with sequence " + updateDto.getSequence() + 
                        " already exists for this question");
            }
            testCase.setSequence(updateDto.getSequence());
        }
        if (updateDto.getInputData() != null) {
            testCase.setInputData(updateDto.getInputData());
        }
        if (updateDto.getExpectedOutput() != null) {
            testCase.setExpectedOutput(updateDto.getExpectedOutput());
        }
        if (updateDto.getIsSample() != null) {
            testCase.setIsSample(updateDto.getIsSample());
        }
        if (updateDto.getExplanation() != null) {
            testCase.setExplanation(updateDto.getExplanation());
        }

        QuestionTestCase savedTestCase = testCaseRepository.save(testCase);
        log.info("Successfully updated test case with ID: {}", savedTestCase.getId());

        return convertToDto(savedTestCase);
    }

    @Override
    public void deleteTestCase(String id) {
        log.info("Soft deleting test case with ID: {}", id);

        QuestionTestCase testCase = testCaseRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException("Test case not found with ID: " + id));

        testCase.setDeleted(true);
        testCaseRepository.save(testCase);

        log.info("Successfully soft deleted test case with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionTestCaseDto> getTestCasesByQuestionId(String questionId) {
        log.info("Retrieving test cases for question ID: {}", questionId);
        List<QuestionTestCase> testCases = testCaseRepository.findByQuestionIdOrderBySequence(questionId);
        return testCases.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionTestCaseDto> getSampleTestCasesByQuestionId(String questionId) {
        log.info("Retrieving sample test cases for question ID: {}", questionId);
        List<QuestionTestCase> testCases = testCaseRepository.findSampleTestCasesByQuestionId(questionId);
        return testCases.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionTestCaseDto> getHiddenTestCasesByQuestionId(String questionId) {
        log.info("Retrieving hidden test cases for question ID: {}", questionId);
        List<QuestionTestCase> testCases = testCaseRepository.findHiddenTestCasesByQuestionId(questionId);
        return testCases.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkSequenceAvailability(String questionId, Integer sequence) {
        log.info("Checking sequence availability: {} for question: {}", sequence, questionId);
        return !testCaseRepository.existsByQuestionIdAndSequence(questionId, sequence);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getNextSequenceNumber(String questionId) {
        log.info("Getting next sequence number for question: {}", questionId);
        Integer maxSequence = testCaseRepository.findMaxSequenceByQuestionId(questionId);
        return maxSequence + 1;
    }

    @Override
    public void reorderTestCases(String questionId, List<TestCaseSequenceDto> testCaseSequences) {
        log.info("Reordering {} test cases for question: {}", testCaseSequences.size(), questionId);

        // Validate question exists
        if (!questionRepository.existsById(questionId)) {
            throw new ResourceNotFoundException("Question not found with ID: " + questionId);
        }

        for (TestCaseSequenceDto sequenceDto : testCaseSequences) {
            QuestionTestCase testCase = testCaseRepository.findByIdAndNotDeleted(sequenceDto.getTestCaseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Test case not found with ID: " + sequenceDto.getTestCaseId()));

            // Ensure test case belongs to the specified question
            if (!testCase.getQuestion().getId().equals(questionId)) {
                throw new IllegalArgumentException("Test case " + sequenceDto.getTestCaseId() + 
                        " does not belong to question " + questionId);
            }

            testCase.setSequence(sequenceDto.getSequence());
            testCaseRepository.save(testCase);
        }

        log.info("Successfully reordered test cases for question: {}", questionId);
    }

    @Override
    public QuestionTestCaseDto toggleSampleStatus(String id, Boolean isSample) {
        log.info("Toggling sample status for test case {} to: {}", id, isSample);

        QuestionTestCase testCase = testCaseRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException("Test case not found with ID: " + id));

        testCase.setIsSample(isSample);
        QuestionTestCase savedTestCase = testCaseRepository.save(testCase);

        log.info("Successfully toggled sample status for test case {}", id);
        return convertToDto(savedTestCase);
    }

    @Override
    public void deleteAllTestCasesForQuestion(String questionId) {
        log.info("Deleting all test cases for question: {}", questionId);

        // Validate question exists
        if (!questionRepository.existsById(questionId)) {
            throw new ResourceNotFoundException("Question not found with ID: " + questionId);
        }

        testCaseRepository.softDeleteByQuestionId(questionId);
        log.info("Successfully deleted all test cases for question: {}", questionId);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getTestCasesCountByQuestionId(String questionId) {
        log.info("Getting test cases count for question: {}", questionId);
        return testCaseRepository.countByQuestionId(questionId);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getSampleTestCasesCountByQuestionId(String questionId) {
        log.info("Getting sample test cases count for question: {}", questionId);
        return testCaseRepository.countSampleTestCasesByQuestionId(questionId);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getHiddenTestCasesCountByQuestionId(String questionId) {
        log.info("Getting hidden test cases count for question: {}", questionId);
        return testCaseRepository.countHiddenTestCasesByQuestionId(questionId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionTestCaseDto> searchTestCasesByInputData(String searchTerm) {
        log.info("Searching test cases by input data: {}", searchTerm);
        List<QuestionTestCase> testCases = testCaseRepository.findByInputDataContainingIgnoreCase(searchTerm);
        return testCases.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionTestCaseDto> searchTestCasesByExpectedOutput(String searchTerm) {
        log.info("Searching test cases by expected output: {}", searchTerm);
        List<QuestionTestCase> testCases = testCaseRepository.findByExpectedOutputContainingIgnoreCase(searchTerm);
        return testCases.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<QuestionTestCaseDto> cloneTestCases(String sourceQuestionId, String targetQuestionId) {
        log.info("Cloning test cases from question {} to question {}", sourceQuestionId, targetQuestionId);

        // Validate both questions exist
        CodingQuestion sourceQuestion = questionRepository.findByIdAndNotDeleted(sourceQuestionId)
                .orElseThrow(() -> new ResourceNotFoundException("Source question not found with ID: " + sourceQuestionId));

        CodingQuestion targetQuestion = questionRepository.findByIdAndNotDeleted(targetQuestionId)
                .orElseThrow(() -> new ResourceNotFoundException("Target question not found with ID: " + targetQuestionId));

        // Get source test cases
        List<QuestionTestCase> sourceTestCases = testCaseRepository.findByQuestionIdOrderBySequence(sourceQuestionId);

        // Clone test cases
        List<QuestionTestCaseDto> clonedTestCases = new ArrayList<>();
        for (QuestionTestCase sourceTestCase : sourceTestCases) {
            QuestionTestCase clonedTestCase = new QuestionTestCase();
            clonedTestCase.setQuestion(targetQuestion);
            clonedTestCase.setSequence(sourceTestCase.getSequence());
            clonedTestCase.setInputData(sourceTestCase.getInputData());
            clonedTestCase.setExpectedOutput(sourceTestCase.getExpectedOutput());
            clonedTestCase.setIsSample(sourceTestCase.getIsSample());
            clonedTestCase.setExplanation(sourceTestCase.getExplanation());

            QuestionTestCase savedTestCase = testCaseRepository.save(clonedTestCase);
            clonedTestCases.add(convertToDto(savedTestCase));
        }

        log.info("Successfully cloned {} test cases from question {} to question {}", 
                clonedTestCases.size(), sourceQuestionId, targetQuestionId);
        return clonedTestCases;
    }

    // Helper method for conversion
    private QuestionTestCaseDto convertToDto(QuestionTestCase testCase) {
        QuestionTestCaseDto dto = new QuestionTestCaseDto();
        dto.setId(testCase.getId());
        dto.setQuestionId(testCase.getQuestion().getId());
        dto.setSequence(testCase.getSequence());
        dto.setInputData(testCase.getInputData());
        dto.setExpectedOutput(testCase.getExpectedOutput());
        dto.setIsSample(testCase.getIsSample());
        dto.setExplanation(testCase.getExplanation());
        dto.setCreatedAt(testCase.getCreatedAt());
        dto.setUpdatedAt(testCase.getUpdatedAt());
        dto.setVersion(testCase.getVersion());

        return dto;
    }
}