package com.codyssey.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

/**
 * Article Entity
 * <p>
 * Represents educational articles about data structures, algorithms, and programming concepts
 * with all associated metadata.
 */
@Entity
@Table(name = "articles",
        indexes = {
                @Index(name = "idx_article_source", columnList = "source_id"),
                @Index(name = "idx_article_status", columnList = "status"),
                @Index(name = "idx_article_created_by", columnList = "created_by_user_id"),
                @Index(name = "idx_article_url_slug", columnList = "url_slug")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Article extends BaseEntity {

    /**
     * Primary key for the entity - Article ID (ART-xxxxxx)
     */
    @Id
    @GeneratedValue(generator = "article-id")
    @GenericGenerator(name = "article-id", strategy = "com.codyssey.api.util.ArticleIdGenerator")
    @Column(name = "id", length = 32)
    private String id;

    /**
     * Article title
     */
    @NotBlank
    @Size(max = 200)
    @Column(name = "title", nullable = false)
    private String title;

    /**
     * Brief summary/description of the article
     */
    @Size(max = 500)
    @Column(name = "short_description")
    private String shortDescription;

    /**
     * Path to the Markdown file containing the full article content
     */
    @NotBlank
    @Size(max = 500)
    @Column(name = "file_path", nullable = false)
    private String filePath;

    /**
     * Source/platform of the article (Internal, External Blog, etc.) - links to Source entity
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_id")
    private Source source;

    /**
     * URL to the original article if it's from an external source
     */
    @Size(max = 500)
    @Column(name = "original_url")
    private String originalUrl;

    /**
     * Article status (ACTIVE, DEPRECATED, DRAFT)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ArticleStatus status = ArticleStatus.ACTIVE;

    /**
     * User who created/added this article
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user_id")
    private User createdByUser;

    /**
     * Associated labels/tags (Data Structures, Algorithms, Topics, etc.)
     */
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ArticleLabel> articleLabels;

    /**
     * SEO-friendly URL slug for this article
     */
    @Size(max = 300)
    @Column(name = "url_slug", unique = true)
    private String urlSlug;

    /**
     * Constructor with required fields
     */
    public Article(String title, String shortDescription, String filePath, Source source, User createdByUser) {
        this.title = title;
        this.shortDescription = shortDescription;
        this.filePath = filePath;
        this.source = source;
        this.createdByUser = createdByUser;
        this.status = ArticleStatus.ACTIVE;
    }

    /**
     * Article status enumeration
     */
    public enum ArticleStatus {
        ACTIVE, DEPRECATED, DRAFT
    }
}