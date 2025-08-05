package com.codyssey.api.service;

import com.codyssey.api.dto.link.QuestionLinkCreateDto;
import com.codyssey.api.dto.link.QuestionLinkBulkCreateDto;
import com.codyssey.api.dto.link.QuestionLinkDto;
import com.codyssey.api.dto.link.LinkSequenceDto;
import com.codyssey.api.dto.question.CodingQuestionSummaryDto;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for QuestionLink operations
 * <p>
 * Defines the contract for question link business logic operations.
 */
public interface QuestionLinkService {

    /**
     * Create a new question link
     *
     * @param createDto link creation data
     * @return created link DTO
     */
    QuestionLinkDto createQuestionLink(QuestionLinkCreateDto createDto);

    /**
     * Create multiple question links in bulk
     *
     * @param bulkCreateDto bulk link creation data
     * @return list of created link DTOs
     */
    List<QuestionLinkDto> createQuestionLinksBulk(QuestionLinkBulkCreateDto bulkCreateDto);

    /**
     * Get outgoing links from a source question
     *
     * @param sourceQuestionId the source question ID
     * @return list of outgoing links
     */
    List<QuestionLinkDto> getOutgoingLinksByQuestionId(String sourceQuestionId);

    /**
     * Get incoming links to a target question
     *
     * @param targetQuestionId the target question ID
     * @return list of incoming links
     */
    List<QuestionLinkDto> getIncomingLinksByQuestionId(String targetQuestionId);

    /**
     * Get question link by ID
     *
     * @param id link ID
     * @return link DTO if found
     */
    Optional<QuestionLinkDto> getLinkById(String id);

    /**
     * Delete question link
     *
     * @param id link ID
     */
    void deleteQuestionLink(String id);

    /**
     * Get follow-up questions
     *
     * @param sourceQuestionId the source question ID
     * @return list of follow-up questions
     */
    List<CodingQuestionSummaryDto> getFollowUpQuestions(String sourceQuestionId);

    /**
     * Get similar questions
     *
     * @param sourceQuestionId the source question ID
     * @return list of similar questions
     */
    List<CodingQuestionSummaryDto> getSimilarQuestions(String sourceQuestionId);

    /**
     * Get prerequisite questions
     *
     * @param sourceQuestionId the source question ID
     * @return list of prerequisite questions
     */
    List<CodingQuestionSummaryDto> getPrerequisiteQuestions(String sourceQuestionId);

    /**
     * Get links by relationship type
     *
     * @param sourceQuestionId the source question ID
     * @param relationshipType the relationship type
     * @return list of links
     */
    List<QuestionLinkDto> getLinksByRelationshipType(String sourceQuestionId, String relationshipType);

    /**
     * Check if sequence is available
     *
     * @param sourceQuestionId the source question ID
     * @param sequence the sequence number
     * @return true if available
     */
    boolean checkSequenceAvailability(String sourceQuestionId, Integer sequence);

    /**
     * Get next available sequence number
     *
     * @param sourceQuestionId the source question ID
     * @return next sequence number
     */
    Integer getNextSequenceNumber(String sourceQuestionId);

    /**
     * Reorder links for a source question
     *
     * @param sourceQuestionId the source question ID
     * @param linkSequences list of link sequences
     */
    void reorderLinks(String sourceQuestionId, List<LinkSequenceDto> linkSequences);

    /**
     * Remove all links for a question
     *
     * @param questionId the question ID
     */
    void removeAllLinksForQuestion(String questionId);

    /**
     * Check if a link exists
     *
     * @param sourceQuestionId the source question ID
     * @param targetQuestionId the target question ID
     * @param relationshipType the relationship type
     * @return true if link exists
     */
    boolean isLinkExists(String sourceQuestionId, String targetQuestionId, String relationshipType);

    /**
     * Get outgoing links count
     *
     * @param sourceQuestionId the source question ID
     * @return count of outgoing links
     */
    Long getOutgoingLinksCount(String sourceQuestionId);

    /**
     * Get incoming links count
     *
     * @param targetQuestionId the target question ID
     * @return count of incoming links
     */
    Long getIncomingLinksCount(String targetQuestionId);
}