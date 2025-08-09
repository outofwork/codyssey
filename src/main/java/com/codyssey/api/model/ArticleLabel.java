package com.codyssey.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

/**
 * ArticleLabel Entity
 * <p>
 * Represents the many-to-many relationship between Articles and Labels
 * with additional metadata like relevance score and primary tag indication.
 */
@Entity
@Table(name = "article_labels",
        indexes = {
                @Index(name = "idx_article_label_article", columnList = "article_id"),
                @Index(name = "idx_article_label_label", columnList = "label_id"),
                @Index(name = "idx_article_label_primary", columnList = "is_primary"),
                @Index(name = "idx_article_label_relevance", columnList = "relevance_score")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_article_label", columnNames = {"article_id", "label_id"})
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArticleLabel extends BaseEntity {

    /**
     * Primary key for the entity - ArticleLabel ID (ALB-xxxxxx)
     */
    @Id
    @GeneratedValue(generator = "article-label-id")
    @GenericGenerator(name = "article-label-id", strategy = "com.codyssey.api.util.ArticleLabelIdGenerator")
    @Column(name = "id", length = 32)
    private String id;

    /**
     * Reference to the article
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    /**
     * Reference to the label (Data Structure, Algorithm, Topic, etc.)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "label_id", nullable = false)
    private Label label;

    /**
     * Relevance score for this label to the article (1-10)
     * Higher score indicates more relevance
     */
    @Column(name = "relevance_score")
    private Integer relevanceScore = 5;

    /**
     * Whether this is a primary/main tag for the article
     */
    @Column(name = "is_primary", nullable = false)
    private Boolean isPrimary = false;

    /**
     * Notes about why this label applies to the article
     */
    @Column(name = "notes", length = 500)
    private String notes;

    /**
     * Constructor with required fields
     */
    public ArticleLabel(Article article, Label label) {
        this.article = article;
        this.label = label;
        this.relevanceScore = 5;
        this.isPrimary = false;
    }

    /**
     * Constructor with relevance score
     */
    public ArticleLabel(Article article, Label label, Integer relevanceScore, Boolean isPrimary) {
        this.article = article;
        this.label = label;
        this.relevanceScore = relevanceScore;
        this.isPrimary = isPrimary;
    }
}