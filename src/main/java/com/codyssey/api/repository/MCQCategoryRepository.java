package com.codyssey.api.repository;

import com.codyssey.api.model.MCQCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for MCQCategory entity
 */
@Repository
public interface MCQCategoryRepository extends JpaRepository<MCQCategory, String> {

    /**
     * Find MCQ categories by MCQ question ID
     */
    List<MCQCategory> findByMcqQuestionId(String mcqQuestionId);

    /**
     * Find MCQ categories by category ID
     */
    List<MCQCategory> findByCategoryId(String categoryId);

    /**
     * Find MCQ categories by MCQ question ID and category ID
     */
    Optional<MCQCategory> findByMcqQuestionIdAndCategoryId(String mcqQuestionId, String categoryId);

    /**
     * Find primary MCQ category for a specific MCQ question
     */
    @Query("SELECT mc FROM MCQCategory mc WHERE mc.mcqQuestion.id = :mcqQuestionId AND mc.isPrimary = true")
    Optional<MCQCategory> findPrimaryByMcqQuestionId(@Param("mcqQuestionId") String mcqQuestionId);

    /**
     * Find MCQ categories by category ID ordered by relevance score (highest first)
     */
    @Query("SELECT mc FROM MCQCategory mc WHERE mc.category.id = :categoryId ORDER BY mc.relevanceScore DESC, mc.isPrimary DESC")
    List<MCQCategory> findByCategoryIdOrderByRelevance(@Param("categoryId") String categoryId);

    /**
     * Find MCQ categories by MCQ question ID ordered by relevance score (highest first)
     */
    @Query("SELECT mc FROM MCQCategory mc WHERE mc.mcqQuestion.id = :mcqQuestionId ORDER BY mc.relevanceScore DESC, mc.isPrimary DESC")
    List<MCQCategory> findByMcqQuestionIdOrderByRelevance(@Param("mcqQuestionId") String mcqQuestionId);

    /**
     * Count MCQ categories by category ID
     */
    long countByCategoryId(String categoryId);

    /**
     * Delete MCQ categories by MCQ question ID and category ID
     */
    void deleteByMcqQuestionIdAndCategoryId(String mcqQuestionId, String categoryId);

    /**
     * Delete all MCQ categories by MCQ question ID
     */
    void deleteByMcqQuestionId(String mcqQuestionId);

    /**
     * Check if MCQ question has a specific category
     */
    boolean existsByMcqQuestionIdAndCategoryId(String mcqQuestionId, String categoryId);

    /**
     * Find MCQ categories by category URL slug
     */
    @Query("SELECT mc FROM MCQCategory mc WHERE mc.category.urlSlug = :categorySlug ORDER BY mc.relevanceScore DESC")
    List<MCQCategory> findByCategorySlugOrderByRelevance(@Param("categorySlug") String categorySlug);
}
