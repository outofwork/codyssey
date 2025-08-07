package com.codyssey.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing an Article
 */
@Entity
@Table(name = "articles")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Article extends BaseEntity {

    @Id
    @Column(name = "article_id", length = 50)
    private String id;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "short_description", length = 500)
    private String shortDescription;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "article_type", nullable = false, length = 50)
    private ArticleType articleType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_label_id")
    private Label categoryLabel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "difficulty_label_id")
    private Label difficultyLabel;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private ArticleStatus status = ArticleStatus.DRAFT;

    @Column(name = "url_slug", unique = true, length = 200)
    private String urlSlug;

    @Column(name = "content_url", length = 500)
    private String contentUrl;

    @Column(name = "reading_time_minutes")
    private Integer readingTimeMinutes;

    @Column(name = "view_count", nullable = false)
    private Integer viewCount = 0;

    @Column(name = "like_count", nullable = false)
    private Integer likeCount = 0;

    @Column(name = "bookmark_count", nullable = false)
    private Integer bookmarkCount = 0;

    // Note: version is inherited from BaseEntity as Long

    @Column(name = "meta_title", length = 200)
    private String metaTitle;

    @Column(name = "meta_description", length = 500)
    private String metaDescription;

    @Column(name = "meta_keywords", length = 500)
    private String metaKeywords;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user_id")
    private User createdByUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by_user_id")
    private User updatedByUser;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ArticleLabel> articleLabels = new ArrayList<>();

    /**
     * Article types enum
     */
    public enum ArticleType {
        DATA_STRUCTURE,
        ALGORITHM,
        SYSTEM_DESIGN,
        PROGRAMMING_CONCEPT,
        INTERVIEW_GUIDE,
        TUTORIAL,
        REFERENCE
    }

    /**
     * Article status enum
     */
    public enum ArticleStatus {
        DRAFT,
        REVIEW,
        PUBLISHED,
        ARCHIVED,
        DEPRECATED
    }

    /**
     * Helper method to add article label
     */
    public void addArticleLabel(ArticleLabel articleLabel) {
        articleLabels.add(articleLabel);
        articleLabel.setArticle(this);
    }

    /**
     * Helper method to remove article label
     */
    public void removeArticleLabel(ArticleLabel articleLabel) {
        articleLabels.remove(articleLabel);
        articleLabel.setArticle(null);
    }

    /**
     * Helper method to clear all article labels
     */
    public void clearArticleLabels() {
        for (ArticleLabel articleLabel : new ArrayList<>(articleLabels)) {
            removeArticleLabel(articleLabel);
        }
    }

    @PrePersist
    @PreUpdate
    private void validateArticle() {
        if (title != null) {
            title = title.trim();
        }
        if (shortDescription != null) {
            shortDescription = shortDescription.trim();
        }
        if (urlSlug != null) {
            urlSlug = urlSlug.trim().toLowerCase();
        }
    }
}