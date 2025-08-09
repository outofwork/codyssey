package com.codyssey.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;

/**
 * MCQCategory Entity
 * <p>
 * Represents the many-to-many relationship between MCQ Questions and Label Categories.
 * This allows an MCQ question to be associated with multiple categories with different
 * relevance scores and metadata.
 */
@Entity
@Table(name = "mcq_categories",
        indexes = {
                @Index(name = "idx_mcq_category_mcq", columnList = "mcq_question_id"),
                @Index(name = "idx_mcq_category_category", columnList = "category_id"),
                @Index(name = "idx_mcq_category_primary", columnList = "is_primary")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MCQCategory extends BaseEntity {

    /**
     * Primary key for the entity - MCQ Category Association ID
     */
    @Id
    @GeneratedValue(generator = "mcq-category-id")
    @GenericGenerator(name = "mcq-category-id", strategy = "com.codyssey.api.util.MCQCategoryIdGenerator")
    @Column(name = "id", length = 32)
    private String id;

    /**
     * Associated MCQ Question
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mcq_question_id", nullable = false)
    @NotNull(message = "MCQ question is required")
    private MCQQuestion mcqQuestion;

    /**
     * Associated Label Category
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @NotNull(message = "Category is required")
    private LabelCategory category;

    /**
     * Relevance score for this category to the MCQ question (0.1 to 1.0)
     * Higher score means more relevant
     */
    @Column(name = "relevance_score", precision = 3, scale = 2)
    @DecimalMin(value = "0.1", message = "Relevance score must be at least 0.1")
    @DecimalMax(value = "1.0", message = "Relevance score must not exceed 1.0")
    private BigDecimal relevanceScore = BigDecimal.valueOf(1.0);

    /**
     * Whether this is the primary category for the MCQ question
     * Only one category per MCQ can be primary
     */
    @Column(name = "is_primary")
    private Boolean isPrimary = false;

    /**
     * Additional notes about why this category applies to the MCQ
     */
    @Size(max = 500, message = "Notes must not exceed 500 characters")
    @Column(name = "notes", length = 500)
    private String notes;

    /**
     * Constructor with required fields
     */
    public MCQCategory(MCQQuestion mcqQuestion, LabelCategory category) {
        this.mcqQuestion = mcqQuestion;
        this.category = category;
        this.relevanceScore = BigDecimal.valueOf(1.0);
        this.isPrimary = false;
    }

    /**
     * Constructor with relevance score
     */
    public MCQCategory(MCQQuestion mcqQuestion, LabelCategory category, BigDecimal relevanceScore, Boolean isPrimary) {
        this.mcqQuestion = mcqQuestion;
        this.category = category;
        this.relevanceScore = relevanceScore != null ? relevanceScore : BigDecimal.valueOf(1.0);
        this.isPrimary = isPrimary != null ? isPrimary : false;
    }
}
