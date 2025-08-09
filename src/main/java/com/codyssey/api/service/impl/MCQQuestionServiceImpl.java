package com.codyssey.api.service.impl;

import com.codyssey.api.dto.mcq.*;
import com.codyssey.api.exception.DuplicateResourceException;
import com.codyssey.api.exception.ResourceNotFoundException;
import com.codyssey.api.model.*;
import com.codyssey.api.repository.*;
import com.codyssey.api.service.MCQQuestionService;
import com.codyssey.api.util.AuthUtils;
import com.codyssey.api.util.UrlSlugGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of MCQQuestionService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MCQQuestionServiceImpl implements MCQQuestionService {

    private final MCQQuestionRepository mcqQuestionRepository;
    private final MCQLabelRepository mcqLabelRepository;
    private final LabelRepository labelRepository;
    private final UserRepository userRepository;

    @Override
    public MCQQuestionDto createMCQQuestion(MCQQuestionCreateDto createDto) {
        log.info("Creating new MCQ question");

        // Get current user
        String currentUserId = AuthUtils.getCurrentUserId();
        User currentUser = null;
        if (currentUserId != null) {
            currentUser = userRepository.findByIdAndNotDeleted(currentUserId)
                    .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));
        }

        // Validate difficulty label if provided
        Label difficultyLabel = null;
        if (createDto.getDifficultyLabelId() != null && !createDto.getDifficultyLabelId().trim().isEmpty()) {
            difficultyLabel = labelRepository.findByIdAndNotDeleted(createDto.getDifficultyLabelId())
                    .orElseThrow(() -> new ResourceNotFoundException("Difficulty label not found with ID: " + createDto.getDifficultyLabelId()));
        }

        // Create MCQ question
        MCQQuestion mcqQuestion = new MCQQuestion(
                createDto.getQuestionText(),
                createDto.getOptionA(),
                createDto.getOptionB(),
                createDto.getOptionC(),
                createDto.getOptionD(),
                createDto.getCorrectAnswer().toUpperCase(),
                createDto.getExplanation(),
                currentUser
        );

        mcqQuestion.setDifficultyLabel(difficultyLabel);

        // Generate URL slug
        String baseSlug = UrlSlugGenerator.generateSlug(createDto.getQuestionText());
        String uniqueSlug = generateUniqueSlug(baseSlug);
        mcqQuestion.setUrlSlug(uniqueSlug);

        MCQQuestion savedQuestion = mcqQuestionRepository.save(mcqQuestion);

        log.info("MCQ question created successfully with ID: {}", savedQuestion.getId());
        return convertToDto(savedQuestion);
    }

    @Override
    @Transactional(readOnly = true)
    public MCQQuestionDto getMCQQuestionById(String id) {
        MCQQuestion mcqQuestion = mcqQuestionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MCQ question not found with ID: " + id));
        return convertToDto(mcqQuestion);
    }

    @Override
    @Transactional(readOnly = true)
    public MCQQuestionDto getMCQQuestionBySlug(String urlSlug) {
        MCQQuestion mcqQuestion = mcqQuestionRepository.findByUrlSlug(urlSlug)
                .orElseThrow(() -> new ResourceNotFoundException("MCQ question not found with slug: " + urlSlug));
        return convertToDto(mcqQuestion);
    }

    @Override
    public MCQQuestionDto updateMCQQuestion(String id, MCQQuestionUpdateDto updateDto) {
        log.info("Updating MCQ question with ID: {}", id);

        MCQQuestion mcqQuestion = mcqQuestionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MCQ question not found with ID: " + id));

        // Update fields if provided
        if (updateDto.getQuestionText() != null) {
            mcqQuestion.setQuestionText(updateDto.getQuestionText());
        }
        if (updateDto.getOptionA() != null) {
            mcqQuestion.setOptionA(updateDto.getOptionA());
        }
        if (updateDto.getOptionB() != null) {
            mcqQuestion.setOptionB(updateDto.getOptionB());
        }
        if (updateDto.getOptionC() != null) {
            mcqQuestion.setOptionC(updateDto.getOptionC());
        }
        if (updateDto.getOptionD() != null) {
            mcqQuestion.setOptionD(updateDto.getOptionD());
        }
        if (updateDto.getCorrectAnswer() != null) {
            mcqQuestion.setCorrectAnswer(updateDto.getCorrectAnswer().toUpperCase());
        }
        if (updateDto.getExplanation() != null) {
            mcqQuestion.setExplanation(updateDto.getExplanation());
        }
        if (updateDto.getStatus() != null) {
            mcqQuestion.setStatus(updateDto.getStatus());
        }

        // Update difficulty label if provided
        if (updateDto.getDifficultyLabelId() != null) {
            if (!updateDto.getDifficultyLabelId().trim().isEmpty()) {
                Label difficultyLabel = labelRepository.findByIdAndNotDeleted(updateDto.getDifficultyLabelId())
                        .orElseThrow(() -> new ResourceNotFoundException("Difficulty label not found with ID: " + updateDto.getDifficultyLabelId()));
                mcqQuestion.setDifficultyLabel(difficultyLabel);
            } else {
                mcqQuestion.setDifficultyLabel(null);
            }
        }

        MCQQuestion savedQuestion = mcqQuestionRepository.save(mcqQuestion);

        log.info("MCQ question updated successfully with ID: {}", savedQuestion.getId());
        return convertToDto(savedQuestion);
    }

    @Override
    public void deleteMCQQuestion(String id) {
        log.info("Deleting MCQ question with ID: {}", id);

        MCQQuestion mcqQuestion = mcqQuestionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MCQ question not found with ID: " + id));

        mcqQuestionRepository.delete(mcqQuestion);

        log.info("MCQ question deleted successfully with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MCQQuestionSummaryDto> getAllMCQQuestions(Pageable pageable) {
        Page<MCQQuestion> mcqQuestions = mcqQuestionRepository.findByStatus(MCQQuestion.MCQStatus.ACTIVE, pageable);
        return mcqQuestions.map(this::convertToSummaryDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MCQQuestionSummaryDto> getMCQQuestionsByLabel(String labelId) {
        // Validate label exists
        labelRepository.findByIdAndNotDeleted(labelId)
                .orElseThrow(() -> new ResourceNotFoundException("Label not found with ID: " + labelId));

        List<MCQQuestion> mcqQuestions = mcqQuestionRepository.findByLabelAndStatus(labelId, MCQQuestion.MCQStatus.ACTIVE);
        return mcqQuestions.stream().map(this::convertToSummaryDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MCQQuestionSummaryDto> getMCQQuestionsByLabel(String labelId, Pageable pageable) {
        // Validate label exists
        labelRepository.findByIdAndNotDeleted(labelId)
                .orElseThrow(() -> new ResourceNotFoundException("Label not found with ID: " + labelId));

        Page<MCQQuestion> mcqQuestions = mcqQuestionRepository.findByLabelAndStatus(labelId, MCQQuestion.MCQStatus.ACTIVE, pageable);
        return mcqQuestions.map(this::convertToSummaryDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MCQQuestionSummaryDto> getMCQQuestionsByLabelHierarchy(String labelId) {
        // Validate label exists
        labelRepository.findByIdAndNotDeleted(labelId)
                .orElseThrow(() -> new ResourceNotFoundException("Label not found with ID: " + labelId));

        List<MCQQuestion> mcqQuestions = mcqQuestionRepository.findByLabelHierarchyAndStatus(labelId, MCQQuestion.MCQStatus.ACTIVE);
        return mcqQuestions.stream().map(this::convertToSummaryDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MCQQuestionSummaryDto> getMCQQuestionsByLabelHierarchy(String labelId, Pageable pageable) {
        // Validate label exists
        labelRepository.findByIdAndNotDeleted(labelId)
                .orElseThrow(() -> new ResourceNotFoundException("Label not found with ID: " + labelId));

        Page<MCQQuestion> mcqQuestions = mcqQuestionRepository.findByLabelHierarchyAndStatus(labelId, MCQQuestion.MCQStatus.ACTIVE, pageable);
        return mcqQuestions.map(this::convertToSummaryDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MCQQuestionSummaryDto> getRandomMCQQuestionsByLabel(String labelId, int count) {
        // Validate label exists
        labelRepository.findByIdAndNotDeleted(labelId)
                .orElseThrow(() -> new ResourceNotFoundException("Label not found with ID: " + labelId));

        List<MCQQuestion> mcqQuestions = mcqQuestionRepository.findRandomByLabel(labelId, MCQQuestion.MCQStatus.ACTIVE.name(), count);
        return mcqQuestions.stream().map(this::convertToSummaryDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MCQQuestionSummaryDto> getRandomMCQQuestionsByLabelHierarchy(String labelId, int count) {
        // Validate label exists
        labelRepository.findByIdAndNotDeleted(labelId)
                .orElseThrow(() -> new ResourceNotFoundException("Label not found with ID: " + labelId));

        List<MCQQuestion> mcqQuestions = mcqQuestionRepository.findRandomByLabelHierarchy(labelId, MCQQuestion.MCQStatus.ACTIVE.name(), count);
        return mcqQuestions.stream().map(this::convertToSummaryDto).collect(Collectors.toList());
    }

    @Override
    public MCQLabelReferenceDto addLabelToMCQQuestion(MCQLabelCreateDto createDto) {
        log.info("Adding label {} to MCQ question {}", createDto.getLabelId(), createDto.getMcqQuestionId());

        // Validate MCQ question exists
        MCQQuestion mcqQuestion = mcqQuestionRepository.findById(createDto.getMcqQuestionId())
                .orElseThrow(() -> new ResourceNotFoundException("MCQ question not found with ID: " + createDto.getMcqQuestionId()));

        // Validate label exists
        Label label = labelRepository.findByIdAndNotDeleted(createDto.getLabelId())
                .orElseThrow(() -> new ResourceNotFoundException("Label not found with ID: " + createDto.getLabelId()));

        // Check if association already exists
        if (mcqLabelRepository.existsByMcqQuestionIdAndLabelId(createDto.getMcqQuestionId(), createDto.getLabelId())) {
            throw new DuplicateResourceException("MCQ question is already associated with this label");
        }

        MCQLabel mcqLabel = new MCQLabel(mcqQuestion, label, createDto.getRelevanceScore(), createDto.getIsPrimary());
        mcqLabel.setNotes(createDto.getNotes());

        MCQLabel savedMcqLabel = mcqLabelRepository.save(mcqLabel);

        log.info("Label added to MCQ question successfully");
        return convertToMcqLabelReferenceDto(savedMcqLabel);
    }

    @Override
    public void removeLabelFromMCQQuestion(String mcqQuestionId, String labelId) {
        log.info("Removing label {} from MCQ question {}", labelId, mcqQuestionId);

        MCQLabel mcqLabel = mcqLabelRepository.findByMcqQuestionIdAndLabelId(mcqQuestionId, labelId)
                .orElseThrow(() -> new ResourceNotFoundException("MCQ question is not associated with this label"));

        mcqLabelRepository.delete(mcqLabel);

        log.info("Label removed from MCQ question successfully");
    }

    @Override
    @Transactional(readOnly = true)
    public long getMCQQuestionCountByLabel(String labelId) {
        // Validate label exists
        labelRepository.findByIdAndNotDeleted(labelId)
                .orElseThrow(() -> new ResourceNotFoundException("Label not found with ID: " + labelId));

        return mcqQuestionRepository.countByLabelAndStatus(labelId, MCQQuestion.MCQStatus.ACTIVE);
    }

    @Override
    @Transactional(readOnly = true)
    public long getMCQQuestionCountByLabelHierarchy(String labelId) {
        // Validate label exists
        labelRepository.findByIdAndNotDeleted(labelId)
                .orElseThrow(() -> new ResourceNotFoundException("Label not found with ID: " + labelId));

        return mcqQuestionRepository.countByLabelHierarchyAndStatus(labelId, MCQQuestion.MCQStatus.ACTIVE);
    }

    // Slug-based methods

    @Override
    @Transactional(readOnly = true)
    public List<MCQQuestionSummaryDto> getMCQQuestionsByLabelSlug(String labelSlug) {
        // Find label by slug
        Label label = labelRepository.findByUrlSlug(labelSlug)
                .orElseThrow(() -> new ResourceNotFoundException("Label not found with slug: " + labelSlug));

        List<MCQQuestion> mcqQuestions = mcqQuestionRepository.findByLabelAndStatus(label.getId(), MCQQuestion.MCQStatus.ACTIVE);
        return mcqQuestions.stream().map(this::convertToSummaryDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MCQQuestionSummaryDto> getMCQQuestionsByLabelSlug(String labelSlug, Pageable pageable) {
        // Find label by slug
        Label label = labelRepository.findByUrlSlug(labelSlug)
                .orElseThrow(() -> new ResourceNotFoundException("Label not found with slug: " + labelSlug));

        Page<MCQQuestion> mcqQuestions = mcqQuestionRepository.findByLabelAndStatus(label.getId(), MCQQuestion.MCQStatus.ACTIVE, pageable);
        return mcqQuestions.map(this::convertToSummaryDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MCQQuestionSummaryDto> getMCQQuestionsByLabelHierarchySlug(String labelSlug) {
        // Find label by slug
        Label label = labelRepository.findByUrlSlug(labelSlug)
                .orElseThrow(() -> new ResourceNotFoundException("Label not found with slug: " + labelSlug));

        List<MCQQuestion> mcqQuestions = mcqQuestionRepository.findByLabelHierarchyAndStatus(label.getId(), MCQQuestion.MCQStatus.ACTIVE);
        return mcqQuestions.stream().map(this::convertToSummaryDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MCQQuestionSummaryDto> getMCQQuestionsByLabelHierarchySlug(String labelSlug, Pageable pageable) {
        // Find label by slug
        Label label = labelRepository.findByUrlSlug(labelSlug)
                .orElseThrow(() -> new ResourceNotFoundException("Label not found with slug: " + labelSlug));

        Page<MCQQuestion> mcqQuestions = mcqQuestionRepository.findByLabelHierarchyAndStatus(label.getId(), MCQQuestion.MCQStatus.ACTIVE, pageable);
        return mcqQuestions.map(this::convertToSummaryDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MCQQuestionSummaryDto> getRandomMCQQuestionsByLabelSlug(String labelSlug, int count) {
        // Find label by slug
        Label label = labelRepository.findByUrlSlug(labelSlug)
                .orElseThrow(() -> new ResourceNotFoundException("Label not found with slug: " + labelSlug));

        List<MCQQuestion> mcqQuestions = mcqQuestionRepository.findRandomByLabel(label.getId(), MCQQuestion.MCQStatus.ACTIVE.name(), count);
        return mcqQuestions.stream().map(this::convertToSummaryDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MCQQuestionSummaryDto> getRandomMCQQuestionsByLabelHierarchySlug(String labelSlug, int count) {
        // Find label by slug
        Label label = labelRepository.findByUrlSlug(labelSlug)
                .orElseThrow(() -> new ResourceNotFoundException("Label not found with slug: " + labelSlug));

        List<MCQQuestion> mcqQuestions = mcqQuestionRepository.findRandomByLabelHierarchy(label.getId(), MCQQuestion.MCQStatus.ACTIVE.name(), count);
        return mcqQuestions.stream().map(this::convertToSummaryDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public long getMCQQuestionCountByLabelSlug(String labelSlug) {
        // Find label by slug
        Label label = labelRepository.findByUrlSlug(labelSlug)
                .orElseThrow(() -> new ResourceNotFoundException("Label not found with slug: " + labelSlug));

        return mcqQuestionRepository.countByLabelAndStatus(label.getId(), MCQQuestion.MCQStatus.ACTIVE);
    }

    @Override
    @Transactional(readOnly = true)
    public long getMCQQuestionCountByLabelHierarchySlug(String labelSlug) {
        // Find label by slug
        Label label = labelRepository.findByUrlSlug(labelSlug)
                .orElseThrow(() -> new ResourceNotFoundException("Label not found with slug: " + labelSlug));

        return mcqQuestionRepository.countByLabelHierarchyAndStatus(label.getId(), MCQQuestion.MCQStatus.ACTIVE);
    }

    private String generateUniqueSlug(String baseSlug) {
        String slug = baseSlug;
        int counter = 1;

        while (mcqQuestionRepository.existsByUrlSlug(slug)) {
            slug = baseSlug + "-" + counter;
            counter++;
        }

        return slug;
    }

    private MCQQuestionDto convertToDto(MCQQuestion mcqQuestion) {
        MCQQuestionDto dto = new MCQQuestionDto();
        dto.setId(mcqQuestion.getId());
        dto.setQuestionText(mcqQuestion.getQuestionText());
        dto.setOptionA(mcqQuestion.getOptionA());
        dto.setOptionB(mcqQuestion.getOptionB());
        dto.setOptionC(mcqQuestion.getOptionC());
        dto.setOptionD(mcqQuestion.getOptionD());
        dto.setCorrectAnswer(mcqQuestion.getCorrectAnswer());
        dto.setExplanation(mcqQuestion.getExplanation());
        dto.setStatus(mcqQuestion.getStatus());
        dto.setUrlSlug(mcqQuestion.getUrlSlug());
        dto.setCreatedAt(mcqQuestion.getCreatedAt());
        dto.setUpdatedAt(mcqQuestion.getUpdatedAt());

        // Convert difficulty label
        if (mcqQuestion.getDifficultyLabel() != null) {
            dto.setDifficultyLabel(convertToLabelSummaryDto(mcqQuestion.getDifficultyLabel()));
        }

        // Convert created by user
        if (mcqQuestion.getCreatedByUser() != null) {
            dto.setCreatedByUser(convertToUserDto(mcqQuestion.getCreatedByUser()));
        }

        // Convert MCQ labels
        if (mcqQuestion.getMcqLabels() != null) {
            dto.setMcqLabels(mcqQuestion.getMcqLabels().stream()
                    .map(this::convertToMcqLabelReferenceDto)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    private MCQQuestionSummaryDto convertToSummaryDto(MCQQuestion mcqQuestion) {
        MCQQuestionSummaryDto dto = new MCQQuestionSummaryDto();
        dto.setId(mcqQuestion.getId());
        dto.setQuestionText(mcqQuestion.getQuestionText());
        dto.setOptionA(mcqQuestion.getOptionA());
        dto.setOptionB(mcqQuestion.getOptionB());
        dto.setOptionC(mcqQuestion.getOptionC());
        dto.setOptionD(mcqQuestion.getOptionD());
        dto.setStatus(mcqQuestion.getStatus());
        dto.setUrlSlug(mcqQuestion.getUrlSlug());
        dto.setCreatedAt(mcqQuestion.getCreatedAt());

        // Convert difficulty label
        if (mcqQuestion.getDifficultyLabel() != null) {
            dto.setDifficultyLabel(convertToLabelSummaryDto(mcqQuestion.getDifficultyLabel()));
        }

        // Convert MCQ labels
        if (mcqQuestion.getMcqLabels() != null) {
            dto.setMcqLabels(mcqQuestion.getMcqLabels().stream()
                    .map(this::convertToMcqLabelReferenceDto)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    private MCQLabelReferenceDto convertToMcqLabelReferenceDto(MCQLabel mcqLabel) {
        MCQLabelReferenceDto dto = new MCQLabelReferenceDto();
        dto.setId(mcqLabel.getId());
        dto.setRelevanceScore(mcqLabel.getRelevanceScore());
        dto.setIsPrimary(mcqLabel.getIsPrimary());
        dto.setNotes(mcqLabel.getNotes());

        if (mcqLabel.getLabel() != null) {
            dto.setLabel(convertToLabelSummaryDto(mcqLabel.getLabel()));
        }

        return dto;
    }

    // Helper methods for DTO conversion
    private com.codyssey.api.dto.label.LabelSummaryDto convertToLabelSummaryDto(Label label) {
        com.codyssey.api.dto.label.LabelSummaryDto dto = new com.codyssey.api.dto.label.LabelSummaryDto();
        dto.setId(label.getId());
        dto.setName(label.getName());
        dto.setDescription(label.getDescription());
        dto.setUrlSlug(label.getUrlSlug());
        return dto;
    }

    private com.codyssey.api.dto.user.UserDto convertToUserDto(User user) {
        com.codyssey.api.dto.user.UserDto dto = new com.codyssey.api.dto.user.UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        return dto;
    }
}
