package com.codyssey.api.service;

import com.codyssey.api.dto.media.*;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for QuestionMedia operations
 * <p>
 * Defines the contract for media file business logic operations.
 */
public interface QuestionMediaService {

    /**
     * Create a new media file
     *
     * @param createDto media creation data
     * @return created media DTO
     */
    QuestionMediaDto createMedia(QuestionMediaCreateDto createDto);

    /**
     * Create multiple media files in bulk
     *
     * @param bulkCreateDto bulk media creation data
     * @return list of created media DTOs
     */
    List<QuestionMediaDto> createMediaBulk(QuestionMediaBulkCreateDto bulkCreateDto);

    /**
     * Get all media files (non-deleted)
     *
     * @return list of all media files
     */
    List<QuestionMediaDto> getAllMedia();

    /**
     * Get media file by ID
     *
     * @param id media ID
     * @return media DTO if found
     */
    Optional<QuestionMediaDto> getMediaById(String id);

    /**
     * Update media file
     *
     * @param id media ID
     * @param updateDto updated media data
     * @return updated media DTO
     */
    QuestionMediaDto updateMedia(String id, QuestionMediaUpdateDto updateDto);

    /**
     * Soft delete media file
     *
     * @param id media ID
     */
    void deleteMedia(String id);

    /**
     * Get media files by question ID
     *
     * @param questionId the question ID
     * @return list of media files ordered by sequence
     */
    List<QuestionMediaDto> getMediaByQuestionId(String questionId);

    /**
     * Get media files by type
     *
     * @param mediaType the media type
     * @return list of media files
     */
    List<QuestionMediaDto> getMediaByType(String mediaType);

    /**
     * Get media files by question ID and type
     *
     * @param questionId the question ID
     * @param mediaType the media type
     * @return list of media files
     */
    List<QuestionMediaDto> getMediaByQuestionIdAndType(String questionId, String mediaType);

    /**
     * Check if sequence is available
     *
     * @param questionId the question ID
     * @param sequence the sequence number
     * @return true if available
     */
    boolean checkSequenceAvailability(String questionId, Integer sequence);

    /**
     * Get next available sequence number
     *
     * @param questionId the question ID
     * @return next sequence number
     */
    Integer getNextSequenceNumber(String questionId);

    /**
     * Reorder media files for a question
     *
     * @param questionId the question ID
     * @param mediaSequences list of media sequences
     */
    void reorderMedia(String questionId, List<MediaSequenceDto> mediaSequences);

    /**
     * Search media files by filename
     *
     * @param searchTerm the search term
     * @return list of matching media files
     */
    List<QuestionMediaDto> searchMediaByFileName(String searchTerm);

    /**
     * Get media files by file path
     *
     * @param filePath the file path
     * @return list of media files
     */
    List<QuestionMediaDto> getMediaByFilePath(String filePath);

    /**
     * Delete all media files for a question
     *
     * @param questionId the question ID
     */
    void deleteAllMediaForQuestion(String questionId);

    /**
     * Get media count by question ID
     *
     * @param questionId the question ID
     * @return count of media files
     */
    Long getMediaCountByQuestionId(String questionId);

    /**
     * Get media count by type
     *
     * @param mediaType the media type
     * @return count of media files
     */
    Long getMediaCountByType(String mediaType);

    /**
     * Get media count by question ID and type
     *
     * @param questionId the question ID
     * @param mediaType the media type
     * @return count of media files
     */
    Long getMediaCountByQuestionIdAndType(String questionId, String mediaType);

    /**
     * Find orphaned media files
     *
     * @return list of orphaned media files
     */
    List<QuestionMediaDto> findOrphanedMediaFiles();

    /**
     * Clone media files from one question to another
     *
     * @param sourceQuestionId the source question ID
     * @param targetQuestionId the target question ID
     * @return list of cloned media files
     */
    List<QuestionMediaDto> cloneMedia(String sourceQuestionId, String targetQuestionId);

    /**
     * Upload media file
     *
     * @param questionId the question ID
     * @param sequence the sequence number
     * @param mediaType the media type
     * @param fileName the file name
     * @param fileData the file data
     * @param description the description
     * @return uploaded media DTO
     */
    QuestionMediaDto uploadMedia(String questionId, Integer sequence, String mediaType, 
                                String fileName, byte[] fileData, String description);

    /**
     * Download media file
     *
     * @param id the media ID
     * @return file data
     */
    byte[] downloadMedia(String id);
}