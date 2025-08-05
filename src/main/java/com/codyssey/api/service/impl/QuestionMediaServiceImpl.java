package com.codyssey.api.service.impl;

import com.codyssey.api.dto.media.*;
import com.codyssey.api.exception.DuplicateResourceException;
import com.codyssey.api.exception.ResourceNotFoundException;
import com.codyssey.api.model.*;
import com.codyssey.api.repository.*;
import com.codyssey.api.service.QuestionMediaService;
import com.codyssey.api.dto.media.QuestionMediaUpdateDto;
import com.codyssey.api.dto.media.MediaSequenceDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of QuestionMediaService
 * <p>
 * Provides media file management functionality including creation, retrieval,
 * updating, deletion, and bulk operations with file upload/download support.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class QuestionMediaServiceImpl implements QuestionMediaService {

    private final QuestionMediaRepository mediaRepository;
    private final CodingQuestionRepository questionRepository;

    // Configuration for file storage (could be moved to application properties)
    private final String UPLOAD_BASE_PATH = "uploads/question-media/";

    @Override
    public QuestionMediaDto createMedia(QuestionMediaCreateDto createDto) {
        log.info("Creating new media file for question: {} with sequence: {}", createDto.getQuestionId(), createDto.getSequence());

        // Validate question exists
        CodingQuestion question = questionRepository.findByIdAndNotDeleted(createDto.getQuestionId())
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with ID: " + createDto.getQuestionId()));

        // Check for sequence conflicts
        if (mediaRepository.existsByQuestionIdAndSequence(createDto.getQuestionId(), createDto.getSequence())) {
            throw new DuplicateResourceException("Media file with sequence " + createDto.getSequence() + 
                    " already exists for question: " + createDto.getQuestionId());
        }

        // Validate media type
        QuestionMedia.MediaType mediaType;
        try {
            mediaType = QuestionMedia.MediaType.valueOf(createDto.getMediaType());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid media type: " + createDto.getMediaType());
        }

        // Create the media entity
        QuestionMedia media = new QuestionMedia();
        media.setQuestion(question);
        media.setSequence(createDto.getSequence());
        media.setMediaType(mediaType);
        media.setFilePath(createDto.getFilePath());
        media.setFileName(createDto.getFileName());
        media.setDescription(createDto.getDescription());

        QuestionMedia savedMedia = mediaRepository.save(media);
        log.info("Successfully created media file with ID: {}", savedMedia.getId());

        return convertToDto(savedMedia);
    }

    @Override
    public List<QuestionMediaDto> createMediaBulk(QuestionMediaBulkCreateDto bulkCreateDto) {
        log.info("Creating {} media files in bulk", bulkCreateDto.getMediaList().size());

        List<QuestionMediaDto> createdMedia = new ArrayList<>();

        for (QuestionMediaCreateDto createDto : bulkCreateDto.getMediaList()) {
            try {
                QuestionMediaDto created = createMedia(createDto);
                createdMedia.add(created);
            } catch (Exception e) {
                log.error("Failed to create media for question: {} - {}", createDto.getQuestionId(), e.getMessage());
                // Continue with other media files but log the error
            }
        }

        log.info("Successfully created {}/{} media files in bulk", 
                createdMedia.size(), bulkCreateDto.getMediaList().size());
        return createdMedia;
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionMediaDto> getAllMedia() {
        log.info("Retrieving all media files");
        List<QuestionMedia> mediaFiles = mediaRepository.findByDeletedFalse();
        return mediaFiles.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<QuestionMediaDto> getMediaById(String id) {
        log.info("Retrieving media file by ID: {}", id);
        return mediaRepository.findByIdAndNotDeleted(id)
                .map(this::convertToDto);
    }

    @Override
    public QuestionMediaDto updateMedia(String id, QuestionMediaUpdateDto updateDto) {
        log.info("Updating media file with ID: {}", id);

        QuestionMedia media = mediaRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException("Media file not found with ID: " + id));

        // Update fields if provided
        if (updateDto.getSequence() != null) {
            // Check for sequence conflicts (excluding current media)
            if (mediaRepository.existsByQuestionIdAndSequenceExcludingId(
                    media.getQuestion().getId(), updateDto.getSequence(), id)) {
                throw new DuplicateResourceException("Media file with sequence " + updateDto.getSequence() + 
                        " already exists for this question");
            }
            media.setSequence(updateDto.getSequence());
        }
        if (updateDto.getMediaType() != null) {
            QuestionMedia.MediaType mediaType = QuestionMedia.MediaType.valueOf(updateDto.getMediaType());
            media.setMediaType(mediaType);
        }
        if (updateDto.getFilePath() != null) {
            media.setFilePath(updateDto.getFilePath());
        }
        if (updateDto.getFileName() != null) {
            media.setFileName(updateDto.getFileName());
        }
        if (updateDto.getDescription() != null) {
            media.setDescription(updateDto.getDescription());
        }

        QuestionMedia savedMedia = mediaRepository.save(media);
        log.info("Successfully updated media file with ID: {}", savedMedia.getId());

        return convertToDto(savedMedia);
    }

    @Override
    public void deleteMedia(String id) {
        log.info("Soft deleting media file with ID: {}", id);

        QuestionMedia media = mediaRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException("Media file not found with ID: " + id));

        media.setDeleted(true);
        mediaRepository.save(media);

        log.info("Successfully soft deleted media file with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionMediaDto> getMediaByQuestionId(String questionId) {
        log.info("Retrieving media files for question ID: {}", questionId);
        List<QuestionMedia> mediaFiles = mediaRepository.findByQuestionIdOrderBySequence(questionId);
        return mediaFiles.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionMediaDto> getMediaByType(String mediaType) {
        log.info("Retrieving media files by type: {}", mediaType);
        
        QuestionMedia.MediaType type;
        try {
            type = QuestionMedia.MediaType.valueOf(mediaType);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid media type: " + mediaType);
        }

        List<QuestionMedia> mediaFiles = mediaRepository.findByMediaType(type);
        return mediaFiles.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionMediaDto> getMediaByQuestionIdAndType(String questionId, String mediaType) {
        log.info("Retrieving media files for question: {} of type: {}", questionId, mediaType);
        
        QuestionMedia.MediaType type;
        try {
            type = QuestionMedia.MediaType.valueOf(mediaType);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid media type: " + mediaType);
        }

        List<QuestionMedia> mediaFiles = mediaRepository.findByQuestionIdAndMediaType(questionId, type);
        return mediaFiles.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkSequenceAvailability(String questionId, Integer sequence) {
        log.info("Checking sequence availability: {} for question: {}", sequence, questionId);
        return !mediaRepository.existsByQuestionIdAndSequence(questionId, sequence);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getNextSequenceNumber(String questionId) {
        log.info("Getting next sequence number for question: {}", questionId);
        Integer maxSequence = mediaRepository.findMaxSequenceByQuestionId(questionId);
        return maxSequence + 1;
    }

    @Override
    public void reorderMedia(String questionId, List<MediaSequenceDto> mediaSequences) {
        log.info("Reordering {} media files for question: {}", mediaSequences.size(), questionId);

        // Validate question exists
        if (!questionRepository.existsById(questionId)) {
            throw new ResourceNotFoundException("Question not found with ID: " + questionId);
        }

        for (MediaSequenceDto sequenceDto : mediaSequences) {
            QuestionMedia media = mediaRepository.findByIdAndNotDeleted(sequenceDto.getMediaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Media file not found with ID: " + sequenceDto.getMediaId()));

            // Ensure media belongs to the specified question
            if (!media.getQuestion().getId().equals(questionId)) {
                throw new IllegalArgumentException("Media file " + sequenceDto.getMediaId() + 
                        " does not belong to question " + questionId);
            }

            media.setSequence(sequenceDto.getSequence());
            mediaRepository.save(media);
        }

        log.info("Successfully reordered media files for question: {}", questionId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionMediaDto> searchMediaByFileName(String searchTerm) {
        log.info("Searching media files by file name: {}", searchTerm);
        List<QuestionMedia> mediaFiles = mediaRepository.findByFileNameContainingIgnoreCase(searchTerm);
        return mediaFiles.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionMediaDto> getMediaByFilePath(String filePath) {
        log.info("Retrieving media files by file path: {}", filePath);
        List<QuestionMedia> mediaFiles = mediaRepository.findByFilePath(filePath);
        return mediaFiles.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAllMediaForQuestion(String questionId) {
        log.info("Deleting all media files for question: {}", questionId);

        // Validate question exists
        if (!questionRepository.existsById(questionId)) {
            throw new ResourceNotFoundException("Question not found with ID: " + questionId);
        }

        mediaRepository.softDeleteByQuestionId(questionId);
        log.info("Successfully deleted all media files for question: {}", questionId);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getMediaCountByQuestionId(String questionId) {
        log.info("Getting media files count for question: {}", questionId);
        return mediaRepository.countByQuestionId(questionId);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getMediaCountByType(String mediaType) {
        log.info("Getting media files count by type: {}", mediaType);
        
        QuestionMedia.MediaType type;
        try {
            type = QuestionMedia.MediaType.valueOf(mediaType);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid media type: " + mediaType);
        }

        return mediaRepository.countByMediaType(type);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getMediaCountByQuestionIdAndType(String questionId, String mediaType) {
        log.info("Getting media files count for question: {} of type: {}", questionId, mediaType);
        
        QuestionMedia.MediaType type;
        try {
            type = QuestionMedia.MediaType.valueOf(mediaType);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid media type: " + mediaType);
        }

        return mediaRepository.countByQuestionIdAndMediaType(questionId, type);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionMediaDto> findOrphanedMediaFiles() {
        log.info("Finding orphaned media files");
        List<QuestionMedia> orphanedMedia = mediaRepository.findOrphanedMediaFiles();
        return orphanedMedia.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<QuestionMediaDto> cloneMedia(String sourceQuestionId, String targetQuestionId) {
        log.info("Cloning media files from question {} to question {}", sourceQuestionId, targetQuestionId);

        // Validate both questions exist
        CodingQuestion sourceQuestion = questionRepository.findByIdAndNotDeleted(sourceQuestionId)
                .orElseThrow(() -> new ResourceNotFoundException("Source question not found with ID: " + sourceQuestionId));

        CodingQuestion targetQuestion = questionRepository.findByIdAndNotDeleted(targetQuestionId)
                .orElseThrow(() -> new ResourceNotFoundException("Target question not found with ID: " + targetQuestionId));

        // Get source media files
        List<QuestionMedia> sourceMediaFiles = mediaRepository.findByQuestionIdOrderBySequence(sourceQuestionId);

        // Clone media files
        List<QuestionMediaDto> clonedMediaList = new ArrayList<>();
        for (QuestionMedia sourceMedia : sourceMediaFiles) {
            QuestionMedia clonedMediaEntity = new QuestionMedia();
            clonedMediaEntity.setQuestion(targetQuestion);
            clonedMediaEntity.setSequence(sourceMedia.getSequence());
            clonedMediaEntity.setMediaType(sourceMedia.getMediaType());
            // Note: File path should be updated to avoid conflicts
            clonedMediaEntity.setFilePath(sourceMedia.getFilePath());
            clonedMediaEntity.setFileName(sourceMedia.getFileName());
            clonedMediaEntity.setDescription(sourceMedia.getDescription());

            QuestionMedia savedMedia = mediaRepository.save(clonedMediaEntity);
            clonedMediaList.add(convertToDto(savedMedia));
        }

        log.info("Successfully cloned {} media files from question {} to question {}", 
                clonedMediaList.size(), sourceQuestionId, targetQuestionId);
        return clonedMediaList;
    }

    @Override
    public QuestionMediaDto uploadMedia(String questionId, Integer sequence, String mediaType, 
                                      String fileName, byte[] fileData, String description) {
        log.info("Uploading media file for question: {} with filename: {}", questionId, fileName);

        try {
            // Create upload directory if it doesn't exist
            Path uploadDir = Paths.get(UPLOAD_BASE_PATH + questionId);
            Files.createDirectories(uploadDir);

            // Generate unique filename to avoid conflicts
            String uniqueFileName = System.currentTimeMillis() + "_" + fileName;
            Path filePath = uploadDir.resolve(uniqueFileName);

            // Write file data
            Files.write(filePath, fileData);

            // Create media entity
            QuestionMediaCreateDto createDto = new QuestionMediaCreateDto();
            createDto.setQuestionId(questionId);
            createDto.setSequence(sequence);
            createDto.setMediaType(mediaType);
            createDto.setFilePath(filePath.toString());
            createDto.setFileName(fileName);
            createDto.setDescription(description);

            QuestionMediaDto savedMedia = createMedia(createDto);
            log.info("Successfully uploaded media file with ID: {}", savedMedia.getId());

            return savedMedia;

        } catch (IOException e) {
            log.error("Failed to upload media file for question: {} - {}", questionId, e.getMessage());
            throw new RuntimeException("Failed to upload media file: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] downloadMedia(String id) {
        log.info("Downloading media file with ID: {}", id);

        QuestionMedia media = mediaRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException("Media file not found with ID: " + id));

        try {
            Path filePath = Paths.get(media.getFilePath());
            if (!Files.exists(filePath)) {
                throw new ResourceNotFoundException("Physical file not found: " + media.getFilePath());
            }

            byte[] fileData = Files.readAllBytes(filePath);
            log.info("Successfully downloaded media file with ID: {}", id);
            return fileData;

        } catch (IOException e) {
            log.error("Failed to download media file with ID: {} - {}", id, e.getMessage());
            throw new RuntimeException("Failed to download media file: " + e.getMessage());
        }
    }

    // Helper method for conversion
    private QuestionMediaDto convertToDto(QuestionMedia media) {
        QuestionMediaDto dto = new QuestionMediaDto();
        dto.setId(media.getId());
        dto.setQuestionId(media.getQuestion().getId());
        dto.setSequence(media.getSequence());
        dto.setMediaType(media.getMediaType().toString());
        dto.setFilePath(media.getFilePath());
        dto.setFileName(media.getFileName());
        dto.setDescription(media.getDescription());
        dto.setCreatedAt(media.getCreatedAt());
        dto.setUpdatedAt(media.getUpdatedAt());
        dto.setVersion(media.getVersion());

        return dto;
    }
}