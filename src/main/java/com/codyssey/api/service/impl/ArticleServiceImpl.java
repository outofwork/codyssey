package com.codyssey.api.service.impl;

import com.codyssey.api.dto.article.*;
import com.codyssey.api.exception.DuplicateResourceException;
import com.codyssey.api.exception.ResourceNotFoundException;
import com.codyssey.api.model.Article;
import com.codyssey.api.model.ArticleLabel;
import com.codyssey.api.model.Label;
import com.codyssey.api.repository.ArticleLabelRepository;
import com.codyssey.api.repository.ArticleRepository;
import com.codyssey.api.repository.LabelRepository;
import com.codyssey.api.service.ArticleService;
import com.codyssey.api.util.ArticleIdGenerator;
import com.codyssey.api.util.ArticleLabelIdGenerator;
import com.codyssey.api.util.UrlSlugGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of ArticleService interface
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleLabelRepository articleLabelRepository;
    private final LabelRepository labelRepository;

    @Override
    public ArticleDto createArticle(ArticleCreateDto createDto) {
        log.info("Creating new article with title: {}", createDto.getTitle());

        // Validate article type
        Article.ArticleType articleType = parseArticleType(createDto.getArticleType());
        
        // Check for duplicate title within same type
        if (articleRepository.existsByTitleAndArticleType(createDto.getTitle(), articleType)) {
            throw new DuplicateResourceException("Article with title '" + createDto.getTitle() + 
                "' already exists for type " + articleType);
        }

        // Generate ID and URL slug
        String articleId = ArticleIdGenerator.generateArticleId();
        String urlSlug = UrlSlugGenerator.generateSlug(createDto.getTitle());
        
        // Ensure URL slug uniqueness
        urlSlug = ensureUniqueUrlSlug(urlSlug);

        // Create article entity
        Article article = new Article();
        article.setId(articleId);
        article.setTitle(createDto.getTitle());
        article.setShortDescription(createDto.getShortDescription());
        article.setContent(createDto.getContent());
        article.setArticleType(articleType);
        article.setStatus(Article.ArticleStatus.DRAFT);
        article.setUrlSlug(urlSlug);
        article.setContentUrl(createDto.getContentUrl());
        article.setReadingTimeMinutes(createDto.getReadingTimeMinutes());
        article.setMetaTitle(createDto.getMetaTitle());
        article.setMetaDescription(createDto.getMetaDescription());
        article.setMetaKeywords(createDto.getMetaKeywords());
        article.setCreatedAt(LocalDateTime.now());
        article.setUpdatedAt(LocalDateTime.now());

        // Set labels if provided
        if (createDto.getCategoryLabelId() != null) {
            Label categoryLabel = labelRepository.findByIdAndNotDeleted(createDto.getCategoryLabelId())
                .orElseThrow(() -> new ResourceNotFoundException("Category label not found"));
            article.setCategoryLabel(categoryLabel);
        }

        if (createDto.getDifficultyLabelId() != null) {
            Label difficultyLabel = labelRepository.findByIdAndNotDeleted(createDto.getDifficultyLabelId())
                .orElseThrow(() -> new ResourceNotFoundException("Difficulty label not found"));
            article.setDifficultyLabel(difficultyLabel);
        }

        Article savedArticle = articleRepository.save(article);
        log.info("Successfully created article with ID: {}", savedArticle.getId());

        return convertToDto(savedArticle);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArticleSummaryDto> getAllArticles() {
        log.debug("Fetching all articles");
        List<Article> articles = articleRepository.findByDeletedFalse();
        return articles.stream()
                .map(this::convertToSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ArticleDto> getArticleById(String id) {
        log.debug("Fetching article by ID: {}", id);
        return articleRepository.findByIdAndNotDeleted(id)
                .map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ArticleDto> getArticleByUrlSlug(String urlSlug) {
        log.debug("Fetching article by URL slug: {}", urlSlug);
        return articleRepository.findByUrlSlugAndDeletedFalse(urlSlug)
                .map(this::convertToDto);
    }

    @Override
    public ArticleDto updateArticle(String id, ArticleUpdateDto updateDto) {
        log.info("Updating article with ID: {}", id);
        
        Article article = articleRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found with ID: " + id));

        updateArticleFields(article, updateDto);
        Article updatedArticle = articleRepository.save(article);
        
        log.info("Successfully updated article with ID: {}", id);
        return convertToDto(updatedArticle);
    }

    @Override
    public ArticleDto updateArticleByUrlSlug(String urlSlug, ArticleUpdateDto updateDto) {
        log.info("Updating article with URL slug: {}", urlSlug);
        
        Article article = articleRepository.findByUrlSlugAndDeletedFalse(urlSlug)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found with URL slug: " + urlSlug));

        updateArticleFields(article, updateDto);
        Article updatedArticle = articleRepository.save(article);
        
        log.info("Successfully updated article with URL slug: {}", urlSlug);
        return convertToDto(updatedArticle);
    }

    @Override
    public void deleteArticle(String id) {
        log.info("Soft deleting article with ID: {}", id);
        
        Article article = articleRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found with ID: " + id));

        article.setDeleted(true);
        article.setUpdatedAt(LocalDateTime.now());
        articleRepository.save(article);
        
        log.info("Successfully soft deleted article with ID: {}", id);
    }

    @Override
    public void deleteArticleByUrlSlug(String urlSlug) {
        log.info("Soft deleting article with URL slug: {}", urlSlug);
        
        Article article = articleRepository.findByUrlSlugAndDeletedFalse(urlSlug)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found with URL slug: " + urlSlug));

        article.setDeleted(true);
        article.setUpdatedAt(LocalDateTime.now());
        articleRepository.save(article);
        
        log.info("Successfully soft deleted article with URL slug: {}", urlSlug);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ArticleSummaryDto> getArticlesWithPagination(Pageable pageable) {
        log.debug("Fetching articles with pagination: {}", pageable);
        Page<Article> articlePage = articleRepository.findByDeletedFalse(pageable);
        return articlePage.map(this::convertToSummaryDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ArticleSummaryDto> searchArticles(String searchTerm, Pageable pageable) {
        log.debug("Searching articles with term: {} and pagination: {}", searchTerm, pageable);
        Page<Article> articlePage = articleRepository.searchByTitleOrDescription(searchTerm, pageable);
        return articlePage.map(this::convertToSummaryDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArticleSummaryDto> getArticlesByType(String articleType) {
        log.debug("Fetching articles by type: {}", articleType);
        Article.ArticleType type = parseArticleType(articleType);
        List<Article> articles = articleRepository.findByArticleTypeAndDeletedFalse(type);
        return articles.stream()
                .map(this::convertToSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArticleSummaryDto> getArticlesByCategoryLabelId(String categoryLabelId) {
        log.debug("Fetching articles by category label ID: {}", categoryLabelId);
        List<Article> articles = articleRepository.findByCategoryLabelId(categoryLabelId);
        return articles.stream()
                .map(this::convertToSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArticleSummaryDto> getArticlesByCategoryLabelSlug(String categoryLabelSlug) {
        log.debug("Fetching articles by category label slug: {}", categoryLabelSlug);
        List<Article> articles = articleRepository.findByCategoryLabelSlug(categoryLabelSlug);
        return articles.stream()
                .map(this::convertToSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArticleSummaryDto> getArticlesByDifficultyLabelId(String difficultyLabelId) {
        log.debug("Fetching articles by difficulty label ID: {}", difficultyLabelId);
        List<Article> articles = articleRepository.findByDifficultyLabelId(difficultyLabelId);
        return articles.stream()
                .map(this::convertToSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArticleSummaryDto> getArticlesByDifficultyLabelSlug(String difficultyLabelSlug) {
        log.debug("Fetching articles by difficulty label slug: {}", difficultyLabelSlug);
        List<Article> articles = articleRepository.findByDifficultyLabelSlug(difficultyLabelSlug);
        return articles.stream()
                .map(this::convertToSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArticleSummaryDto> getArticlesByLabelId(String labelId) {
        log.debug("Fetching articles by label ID: {}", labelId);
        List<Article> articles = articleRepository.findByLabelId(labelId);
        return articles.stream()
                .map(this::convertToSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArticleSummaryDto> getArticlesByLabelSlug(String labelSlug) {
        log.debug("Fetching articles by label slug: {}", labelSlug);
        List<Article> articles = articleRepository.findByLabelSlug(labelSlug);
        return articles.stream()
                .map(this::convertToSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean createArticleLabel(ArticleLabelCreateDto createDto) {
        log.info("Creating article-label relationship: article={}, label={}", 
                createDto.getArticleId(), createDto.getLabelId());

        Article article = articleRepository.findByIdAndNotDeleted(createDto.getArticleId())
                .orElseThrow(() -> new ResourceNotFoundException("Article not found"));

        Label label = labelRepository.findByIdAndNotDeleted(createDto.getLabelId())
                .orElseThrow(() -> new ResourceNotFoundException("Label not found"));

        // Check if relationship already exists
        boolean exists = articleLabelRepository.existsByArticleIdAndLabelId(
                createDto.getArticleId(), createDto.getLabelId());
        if (exists) {
            throw new DuplicateResourceException("Article-Label relationship already exists");
        }

        String articleLabelId = ArticleLabelIdGenerator.generateArticleLabelId();
        ArticleLabel articleLabel = new ArticleLabel(articleLabelId, article, label);
        
        if (createDto.getDisplayOrder() != null) {
            articleLabel.setDisplayOrder(createDto.getDisplayOrder());
        }
        if (createDto.getIsPrimary() != null) {
            articleLabel.setIsPrimary(createDto.getIsPrimary());
        }

        articleLabel.setCreatedAt(LocalDateTime.now());
        articleLabel.setUpdatedAt(LocalDateTime.now());

        articleLabelRepository.save(articleLabel);
        log.info("Successfully created article-label relationship with ID: {}", articleLabelId);
        
        return true;
    }

    @Override
    public int createArticleLabels(ArticleLabelBulkCreateDto bulkCreateDto) {
        log.info("Creating bulk article-label relationships for article: {}", bulkCreateDto.getArticleId());

        Article article = articleRepository.findByIdAndNotDeleted(bulkCreateDto.getArticleId())
                .orElseThrow(() -> new ResourceNotFoundException("Article not found"));

        int createdCount = 0;
        for (String labelId : bulkCreateDto.getLabelIds()) {
            Label label = labelRepository.findByIdAndNotDeleted(labelId)
                    .orElseThrow(() -> new ResourceNotFoundException("Label not found: " + labelId));

            // Skip if relationship already exists
            if (articleLabelRepository.existsByArticleIdAndLabelId(bulkCreateDto.getArticleId(), labelId)) {
                log.warn("Article-Label relationship already exists: article={}, label={}", 
                        bulkCreateDto.getArticleId(), labelId);
                continue;
            }

            String articleLabelId = ArticleLabelIdGenerator.generateArticleLabelId();
            ArticleLabel articleLabel = new ArticleLabel(articleLabelId, article, label);
            articleLabel.setCreatedAt(LocalDateTime.now());
            articleLabel.setUpdatedAt(LocalDateTime.now());

            articleLabelRepository.save(articleLabel);
            createdCount++;
        }

        log.info("Successfully created {} article-label relationships", createdCount);
        return createdCount;
    }

    @Override
    public boolean removeArticleLabel(String articleId, String labelId) {
        log.info("Removing article-label relationship: article={}, label={}", articleId, labelId);

        Optional<ArticleLabel> articleLabelOpt = articleLabelRepository.findByArticleIdAndLabelId(articleId, labelId);
        if (articleLabelOpt.isEmpty()) {
            throw new ResourceNotFoundException("Article-Label relationship not found");
        }

        articleLabelRepository.delete(articleLabelOpt.get());
        log.info("Successfully removed article-label relationship");
        
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public ArticleStatisticsDto getArticleStatistics() {
        log.debug("Fetching article statistics");

        List<Article> allArticles = articleRepository.findByDeletedFalse();
        long totalArticles = allArticles.size();

        Map<String, Long> articlesByType = allArticles.stream()
                .collect(Collectors.groupingBy(
                        article -> article.getArticleType().name(),
                        Collectors.counting()));

        Map<String, Long> articlesByStatus = allArticles.stream()
                .collect(Collectors.groupingBy(
                        article -> article.getStatus().name(),
                        Collectors.counting()));

        // Get difficulty distribution
        Map<String, Long> articlesByDifficulty = allArticles.stream()
                .filter(article -> article.getDifficultyLabel() != null)
                .collect(Collectors.groupingBy(
                        article -> article.getDifficultyLabel().getName(),
                        Collectors.counting()));

        ArticleStatisticsDto stats = new ArticleStatisticsDto();
        stats.setTotalArticles(totalArticles);
        stats.setArticlesByType(articlesByType);
        stats.setArticlesByStatus(articlesByStatus);
        stats.setArticlesByDifficulty(articlesByDifficulty);

        return stats;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ArticleSummaryDto> getRecentArticles(Pageable pageable) {
        log.debug("Fetching recent articles with pagination: {}", pageable);
        Page<Article> articlePage = articleRepository.findRecentArticles(pageable);
        return articlePage.map(this::convertToSummaryDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArticleSummaryDto> searchWithFilters(List<String> articleTypes,
                                                    List<String> categoryLabelIds,
                                                    List<String> difficultyLabelIds,
                                                    List<String> labelIds,
                                                    String searchTerm) {
        log.debug("Searching articles with filters");

        List<Article.ArticleType> types = null;
        if (articleTypes != null && !articleTypes.isEmpty()) {
            types = articleTypes.stream()
                    .map(this::parseArticleType)
                    .collect(Collectors.toList());
        }

        List<Article> articles = articleRepository.searchWithFilters(
                types, categoryLabelIds, difficultyLabelIds, labelIds, searchTerm);

        return articles.stream()
                .map(this::convertToSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    public void incrementViewCount(String id) {
        Article article = articleRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found"));
        
        article.setViewCount(article.getViewCount() + 1);
        article.setUpdatedAt(LocalDateTime.now());
        articleRepository.save(article);
    }

    @Override
    public void incrementLikeCount(String id) {
        Article article = articleRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found"));
        
        article.setLikeCount(article.getLikeCount() + 1);
        article.setUpdatedAt(LocalDateTime.now());
        articleRepository.save(article);
    }

    @Override
    public void incrementBookmarkCount(String id) {
        Article article = articleRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found"));
        
        article.setBookmarkCount(article.getBookmarkCount() + 1);
        article.setUpdatedAt(LocalDateTime.now());
        articleRepository.save(article);
    }

    @Override
    public ArticleDto convertToDto(Article article) {
        if (article == null) {
            return null;
        }

        ArticleDto dto = new ArticleDto();
        dto.setId(article.getId());
        dto.setTitle(article.getTitle());
        dto.setShortDescription(article.getShortDescription());
        dto.setArticleType(article.getArticleType().name());
        dto.setStatus(article.getStatus().name());
        dto.setReadingTimeMinutes(article.getReadingTimeMinutes());
        dto.setContentUrl(article.getContentUrl());
        dto.setVersion(article.getVersion());

        // Set category labels
        if (article.getCategoryLabel() != null) {
            List<ArticleLabelReferenceDto> categoryLabels = new ArrayList<>();
            ArticleLabelReferenceDto categoryRef = new ArticleLabelReferenceDto();
            categoryRef.setId(article.getCategoryLabel().getId());
            categoryRef.setName(article.getCategoryLabel().getName());
            categoryRef.setUrlSlug(article.getCategoryLabel().getUrlSlug());
            categoryLabels.add(categoryRef);
            dto.setCategoryLabels(categoryLabels);
        }

        // Set difficulty label
        if (article.getDifficultyLabel() != null) {
            ArticleLabelReferenceDto difficultyRef = new ArticleLabelReferenceDto();
            difficultyRef.setId(article.getDifficultyLabel().getId());
            difficultyRef.setName(article.getDifficultyLabel().getName());
            difficultyRef.setUrlSlug(article.getDifficultyLabel().getUrlSlug());
            dto.setDifficultyLabel(difficultyRef);
        }

        return dto;
    }

    @Override
    public ArticleSummaryDto convertToSummaryDto(Article article) {
        if (article == null) {
            return null;
        }

        ArticleSummaryDto dto = new ArticleSummaryDto();
        dto.setId(article.getId());
        dto.setTitle(article.getTitle());
        dto.setShortDescription(article.getShortDescription());
        dto.setArticleType(article.getArticleType().name());
        dto.setStatus(article.getStatus().name());
        dto.setReadingTimeMinutes(article.getReadingTimeMinutes());
        dto.setViewCount(article.getViewCount());
        dto.setLikeCount(article.getLikeCount());

        // Set category labels
        if (article.getCategoryLabel() != null) {
            List<ArticleLabelReferenceDto> categoryLabels = new ArrayList<>();
            ArticleLabelReferenceDto categoryRef = new ArticleLabelReferenceDto();
            categoryRef.setId(article.getCategoryLabel().getId());
            categoryRef.setName(article.getCategoryLabel().getName());
            categoryRef.setUrlSlug(article.getCategoryLabel().getUrlSlug());
            categoryLabels.add(categoryRef);
            dto.setCategoryLabels(categoryLabels);
        }

        // Set difficulty label
        if (article.getDifficultyLabel() != null) {
            ArticleLabelReferenceDto difficultyRef = new ArticleLabelReferenceDto();
            difficultyRef.setId(article.getDifficultyLabel().getId());
            difficultyRef.setName(article.getDifficultyLabel().getName());
            difficultyRef.setUrlSlug(article.getDifficultyLabel().getUrlSlug());
            dto.setDifficultyLabel(difficultyRef);
        }

        return dto;
    }

    // Helper methods

    private Article.ArticleType parseArticleType(String articleType) {
        try {
            return Article.ArticleType.valueOf(articleType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid article type: " + articleType);
        }
    }

    private String ensureUniqueUrlSlug(String baseSlug) {
        String slug = baseSlug;
        int counter = 1;
        
        while (articleRepository.findByUrlSlugAndDeletedFalse(slug).isPresent()) {
            slug = baseSlug + "-" + counter;
            counter++;
        }
        
        return slug;
    }

    private void updateArticleFields(Article article, ArticleUpdateDto updateDto) {
        if (updateDto.getTitle() != null) {
            // Check for duplicate title within same type (excluding current article)
            if (articleRepository.existsByTitleAndArticleTypeExcludingId(
                    updateDto.getTitle(), article.getArticleType(), article.getId())) {
                throw new DuplicateResourceException("Article with title '" + updateDto.getTitle() + 
                    "' already exists for type " + article.getArticleType());
            }
            article.setTitle(updateDto.getTitle());
            
            // Update URL slug if title changed
            String newSlug = UrlSlugGenerator.generateSlug(updateDto.getTitle());
            if (!newSlug.equals(article.getUrlSlug())) {
                article.setUrlSlug(ensureUniqueUrlSlug(newSlug));
            }
        }

        if (updateDto.getShortDescription() != null) {
            article.setShortDescription(updateDto.getShortDescription());
        }

        if (updateDto.getContent() != null) {
            article.setContent(updateDto.getContent());
        }

        if (updateDto.getArticleType() != null) {
            article.setArticleType(parseArticleType(updateDto.getArticleType()));
        }

        if (updateDto.getStatus() != null) {
            try {
                article.setStatus(Article.ArticleStatus.valueOf(updateDto.getStatus().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid article status: " + updateDto.getStatus());
            }
        }

        if (updateDto.getContentUrl() != null) {
            article.setContentUrl(updateDto.getContentUrl());
        }

        if (updateDto.getReadingTimeMinutes() != null) {
            article.setReadingTimeMinutes(updateDto.getReadingTimeMinutes());
        }

        if (updateDto.getMetaTitle() != null) {
            article.setMetaTitle(updateDto.getMetaTitle());
        }

        if (updateDto.getMetaDescription() != null) {
            article.setMetaDescription(updateDto.getMetaDescription());
        }

        if (updateDto.getMetaKeywords() != null) {
            article.setMetaKeywords(updateDto.getMetaKeywords());
        }

        article.setUpdatedAt(LocalDateTime.now());
    }
}