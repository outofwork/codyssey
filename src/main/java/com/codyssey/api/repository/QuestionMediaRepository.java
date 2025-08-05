package com.codyssey.api.repository;

import com.codyssey.api.model.CodingQuestion;
import com.codyssey.api.model.QuestionMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for QuestionMedia entity
 * <p>
 * Provides data access methods for QuestionMedia entities with
 * additional custom query methods for searching and filtering.
 */
@Repository
public interface QuestionMediaRepository extends JpaRepository<QuestionMedia, String> {

    /**
     * Find all media files that are not soft deleted
     *
     * @return List of all non-deleted media files
     */
    List<QuestionMedia> findByDeletedFalse();

    /**
     * Find media file by ID and not deleted
     *
     * @param id the ID to search for
     * @return Optional containing the media file if found and not deleted
     */
    @Query("SELECT qm FROM QuestionMedia qm WHERE qm.id = :id AND qm.deleted = false")
    Optional<QuestionMedia> findByIdAndNotDeleted(@Param("id") String id);

    /**
     * Find media files by question ID ordered by sequence
     *
     * @param questionId the question ID
     * @return List of media files for the question ordered by sequence
     */
    @Query("SELECT qm FROM QuestionMedia qm WHERE qm.question.id = :questionId AND qm.deleted = false ORDER BY qm.sequence")
    List<QuestionMedia> findByQuestionIdOrderBySequence(@Param("questionId") String questionId);

    /**
     * Find media files by question ordered by sequence
     *
     * @param question the question
     * @return List of media files for the question ordered by sequence
     */
    @Query("SELECT qm FROM QuestionMedia qm WHERE qm.question = :question AND qm.deleted = false ORDER BY qm.sequence")
    List<QuestionMedia> findByQuestionOrderBySequence(@Param("question") CodingQuestion question);

    /**
     * Find media files by media type
     *
     * @param mediaType the media type
     * @return List of media files of the specified type
     */
    @Query("SELECT qm FROM QuestionMedia qm WHERE qm.mediaType = :mediaType AND qm.deleted = false")
    List<QuestionMedia> findByMediaType(@Param("mediaType") QuestionMedia.MediaType mediaType);

    /**
     * Find media files by question ID and media type
     *
     * @param questionId the question ID
     * @param mediaType the media type
     * @return List of media files for the question of the specified type
     */
    @Query("SELECT qm FROM QuestionMedia qm WHERE qm.question.id = :questionId AND qm.mediaType = :mediaType AND qm.deleted = false ORDER BY qm.sequence")
    List<QuestionMedia> findByQuestionIdAndMediaType(@Param("questionId") String questionId, @Param("mediaType") QuestionMedia.MediaType mediaType);

    /**
     * Check if a media file exists for a question at a specific sequence
     *
     * @param questionId the question ID
     * @param sequence the sequence number
     * @return true if a media file exists at the sequence
     */
    @Query("SELECT COUNT(qm) > 0 FROM QuestionMedia qm WHERE qm.question.id = :questionId AND qm.sequence = :sequence AND qm.deleted = false")
    boolean existsByQuestionIdAndSequence(@Param("questionId") String questionId, @Param("sequence") Integer sequence);

    /**
     * Check if a media file exists for a question at a specific sequence (excluding a specific media ID for updates)
     *
     * @param questionId the question ID
     * @param sequence the sequence number
     * @param excludeId the media ID to exclude from the check
     * @return true if a media file exists at the sequence (excluding the specified ID)
     */
    @Query("SELECT COUNT(qm) > 0 FROM QuestionMedia qm WHERE qm.question.id = :questionId AND qm.sequence = :sequence AND qm.deleted = false AND qm.id != :excludeId")
    boolean existsByQuestionIdAndSequenceExcludingId(@Param("questionId") String questionId, @Param("sequence") Integer sequence, @Param("excludeId") String excludeId);

    /**
     * Find media files by file path
     *
     * @param filePath the file path
     * @return List of media files with the specified file path
     */
    @Query("SELECT qm FROM QuestionMedia qm WHERE qm.filePath = :filePath AND qm.deleted = false")
    List<QuestionMedia> findByFilePath(@Param("filePath") String filePath);

    /**
     * Find media files by file name containing the search term (case insensitive)
     *
     * @param searchTerm the search term
     * @return List of media files with matching file names
     */
    @Query("SELECT qm FROM QuestionMedia qm WHERE LOWER(qm.fileName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) AND qm.deleted = false")
    List<QuestionMedia> findByFileNameContainingIgnoreCase(@Param("searchTerm") String searchTerm);

    /**
     * Get the maximum sequence number for media files of a question
     *
     * @param questionId the question ID
     * @return the maximum sequence number, or 0 if no media files exist
     */
    @Query("SELECT COALESCE(MAX(qm.sequence), 0) FROM QuestionMedia qm WHERE qm.question.id = :questionId AND qm.deleted = false")
    Integer findMaxSequenceByQuestionId(@Param("questionId") String questionId);

    /**
     * Count media files by question ID
     *
     * @param questionId the question ID
     * @return count of media files for the question
     */
    @Query("SELECT COUNT(qm) FROM QuestionMedia qm WHERE qm.question.id = :questionId AND qm.deleted = false")
    Long countByQuestionId(@Param("questionId") String questionId);

    /**
     * Count media files by media type
     *
     * @param mediaType the media type
     * @return count of media files of the specified type
     */
    @Query("SELECT COUNT(qm) FROM QuestionMedia qm WHERE qm.mediaType = :mediaType AND qm.deleted = false")
    Long countByMediaType(@Param("mediaType") QuestionMedia.MediaType mediaType);

    /**
     * Count media files by question ID and media type
     *
     * @param questionId the question ID
     * @param mediaType the media type
     * @return count of media files for the question of the specified type
     */
    @Query("SELECT COUNT(qm) FROM QuestionMedia qm WHERE qm.question.id = :questionId AND qm.mediaType = :mediaType AND qm.deleted = false")
    Long countByQuestionIdAndMediaType(@Param("questionId") String questionId, @Param("mediaType") QuestionMedia.MediaType mediaType);

    /**
     * Delete all media files for a question (soft delete)
     *
     * @param questionId the question ID
     */
    @Query("UPDATE QuestionMedia qm SET qm.deleted = true WHERE qm.question.id = :questionId")
    void softDeleteByQuestionId(@Param("questionId") String questionId);

    /**
     * Find orphaned media files (files that exist but question is deleted)
     *
     * @return List of orphaned media files
     */
    @Query("SELECT qm FROM QuestionMedia qm WHERE qm.question.deleted = true AND qm.deleted = false")
    List<QuestionMedia> findOrphanedMediaFiles();
}