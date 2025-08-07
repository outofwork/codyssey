package com.codyssey.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Entity representing the many-to-many relationship between Article and Label
 */
@Entity
@Table(name = "article_labels")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ArticleLabel extends BaseEntity {

    @Id
    @Column(name = "article_label_id", length = 50)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "label_id", nullable = false)
    private Label label;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Column(name = "is_primary", nullable = false)
    private Boolean isPrimary = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user_id")
    private User createdByUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by_user_id")
    private User updatedByUser;

    /**
     * Constructor with required fields
     */
    public ArticleLabel(String id, Article article, Label label) {
        this.id = id;
        this.article = article;
        this.label = label;
        this.isPrimary = false;
    }

    /**
     * Constructor with primary flag
     */
    public ArticleLabel(String id, Article article, Label label, Boolean isPrimary) {
        this.id = id;
        this.article = article;
        this.label = label;
        this.isPrimary = isPrimary != null ? isPrimary : false;
    }

    /**
     * Constructor with display order
     */
    public ArticleLabel(String id, Article article, Label label, Integer displayOrder, Boolean isPrimary) {
        this.id = id;
        this.article = article;
        this.label = label;
        this.displayOrder = displayOrder;
        this.isPrimary = isPrimary != null ? isPrimary : false;
    }

    @PrePersist
    @PreUpdate
    private void validateArticleLabel() {
        if (isPrimary == null) {
            isPrimary = false;
        }
        if (displayOrder != null && displayOrder < 0) {
            displayOrder = 0;
        }
    }

    @Override
    public String toString() {
        return "ArticleLabel{" +
                "id='" + id + '\'' +
                ", articleId='" + (article != null ? article.getId() : null) + '\'' +
                ", labelId='" + (label != null ? label.getId() : null) + '\'' +
                ", displayOrder=" + displayOrder +
                ", isPrimary=" + isPrimary +
                '}';
    }
}