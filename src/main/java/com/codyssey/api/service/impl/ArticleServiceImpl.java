package com.codyssey.api.service.impl;

import com.codyssey.api.dto.article.*;
import com.codyssey.api.dto.label.LabelSummaryDto;
import com.codyssey.api.dto.source.SourceSummaryDto;
import com.codyssey.api.dto.user.UserDto;
import com.codyssey.api.exception.DuplicateResourceException;
import com.codyssey.api.exception.ResourceNotFoundException;
import com.codyssey.api.model.*;
import com.codyssey.api.repository.*;
import com.codyssey.api.service.ArticleService;
import com.codyssey.api.util.UrlSlugGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of ArticleService
 * <p>
 * Provides article management functionality including
 * creation, retrieval, updating, and deletion with comprehensive search capabilities.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleLabelRepository articleLabelRepository;
    private final LabelRepository labelRepository;
    private final UserRepository userRepository;
    private final SourceRepository sourceRepository;

    @Override
    public ArticleDto createArticle(ArticleCreateDto createDto) {
        log.info("Creating new article with title: {}", createDto.getTitle());

        // Validate source if provided
        Source source = null;
        if (createDto.getSourceId() != null && !createDto.getSourceId().trim().isEmpty()) {
            source = sourceRepository.findByIdAndNotDeleted(createDto.getSourceId())
                    .orElseThrow(() -> new ResourceNotFoundException("Source not found with ID: " + createDto.getSourceId()));
        }

        // Validate user if provided
        User createdByUser = null;
        if (createDto.getCreatedByUserId() != null && !createDto.getCreatedByUserId().trim().isEmpty()) {
            createdByUser = userRepository.findByIdAndNotDeleted(createDto.getCreatedByUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + createDto.getCreatedByUserId()));
        }

        // Check for duplicate title within source
        if (source != null) {
            if (articleRepository.existsByTitleAndSourceId(createDto.getTitle(), source.getId())) {
                throw new DuplicateResourceException("Article with title '" + createDto.getTitle() + 
                        "' already exists for source: " + source.getName());
            }
        }

        // Create the article entity
        Article article = new Article();
        article.setTitle(createDto.getTitle());
        article.setShortDescription(createDto.getShortDescription());
        article.setFilePath(createDto.getFilePath());
        article.setSource(source);
        article.setOriginalUrl(createDto.getOriginalUrl());
        article.setStatus(Article.ArticleStatus.valueOf(createDto.getStatus()));
        article.setCreatedByUser(createdByUser);
        
        // Generate unique URL slug
        String sourceCode = source != null ? source.getCode() : "";
        String baseSlug = UrlSlugGenerator.generateArticleSlug(createDto.getTitle(), sourceCode);
        String uniqueSlug = generateUniqueSlug(baseSlug);
        article.setUrlSlug(uniqueSlug);

        Article savedArticle = articleRepository.save(article);
        log.info("Successfully created article with ID: {}", savedArticle.getId());

        return convertToDto(savedArticle);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArticleSummaryDto> getAllArticles() {
        log.info("Retrieving all articles");
        List<Article> articles = articleRepository.findByDeletedFalse();
        return articles.stream()
                .map(this::convertToSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ArticleDto> getArticleById(String id) {
        log.info("Retrieving article by ID: {}", id);
        
        // First get the basic article
        Optional<Article> articleOpt = articleRepository.findByIdAndNotDeleted(id);
        if (articleOpt.isEmpty()) {
            return Optional.empty();
        }
        
        Article article = articleOpt.get();
        
        // Load labels separately to avoid potential fetch issues
        List<ArticleLabel> articleLabels = articleLabelRepository.findByArticleId(id);
        
        // Manually set the loaded collections
        article.setArticleLabels(articleLabels);
        
        return Optional.of(convertToDto(article));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ArticleDto> getArticleByUrlSlug(String urlSlug) {
        log.info("Retrieving article by URL slug: {}", urlSlug);
        
        // First get the basic article
        Optional<Article> articleOpt = articleRepository.findByUrlSlug(urlSlug);
        if (articleOpt.isEmpty()) {
            return Optional.empty();
        }
        
        Article article = articleOpt.get();
        
        // Load labels separately
        List<ArticleLabel> articleLabels = articleLabelRepository.findByArticleId(article.getId());
        
        // Manually set the loaded collections
        article.setArticleLabels(articleLabels);
        
        return Optional.of(convertToDto(article));
    }

    @Override
    public ArticleDto updateArticle(String id, ArticleUpdateDto updateDto) {
        log.info("Updating article with ID: {}", id);

        Article article = articleRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found with ID: " + id));

        // Update fields if provided
        if (updateDto.getTitle() != null) {
            article.setTitle(updateDto.getTitle());
        }
        if (updateDto.getShortDescription() != null) {
            article.setShortDescription(updateDto.getShortDescription());
        }
        if (updateDto.getFilePath() != null) {
            article.setFilePath(updateDto.getFilePath());
        }
        if (updateDto.getSourceId() != null) {
            Source source = sourceRepository.findByIdAndNotDeleted(updateDto.getSourceId())
                    .orElseThrow(() -> new ResourceNotFoundException("Source not found with ID: " + updateDto.getSourceId()));
            article.setSource(source);
        }
        if (updateDto.getOriginalUrl() != null) {
            article.setOriginalUrl(updateDto.getOriginalUrl());
        }
        if (updateDto.getStatus() != null) {
            article.setStatus(Article.ArticleStatus.valueOf(updateDto.getStatus()));
        }

        // Regenerate URL slug if title was updated
        if (updateDto.getTitle() != null) {
            String sourceCode = article.getSource() != null ? article.getSource().getCode() : "";
            String baseSlug = UrlSlugGenerator.generateArticleSlug(updateDto.getTitle(), sourceCode);
            String uniqueSlug = generateUniqueSlug(baseSlug, article.getId());
            article.setUrlSlug(uniqueSlug);
        }

        Article savedArticle = articleRepository.save(article);
        log.info("Successfully updated article with ID: {}", savedArticle.getId());

        return convertToDto(savedArticle);
    }

    @Override
    public ArticleDto updateArticleByUrlSlug(String urlSlug, ArticleUpdateDto updateDto) {
        log.info("Updating article with URL slug: {}", urlSlug);

        Article article = articleRepository.findByUrlSlug(urlSlug)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found with URL slug: " + urlSlug));

        return updateArticle(article.getId(), updateDto);
    }

    @Override
    public void deleteArticle(String id) {
        log.info("Soft deleting article with ID: {}", id);

        Article article = articleRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found with ID: " + id));

        article.setDeleted(true);
        articleRepository.save(article);

        log.info("Successfully soft deleted article with ID: {}", id);
    }

    @Override
    public void deleteArticleByUrlSlug(String urlSlug) {
        log.info("Soft deleting article with URL slug: {}", urlSlug);

        Article article = articleRepository.findByUrlSlug(urlSlug)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found with URL slug: " + urlSlug));

        deleteArticle(article.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ArticleSummaryDto> getArticlesWithPagination(Pageable pageable) {
        log.info("Retrieving articles with pagination");
        Page<Article> articles = articleRepository.findArticlesWithPagination(pageable);
        return articles.map(this::convertToSummaryDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ArticleSummaryDto> searchArticles(String searchTerm, Pageable pageable) {
        log.info("Searching articles with term: {}", searchTerm);
        Page<Article> articles = articleRepository.searchByTitleOrDescription(searchTerm, pageable);
        return articles.map(this::convertToSummaryDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArticleSummaryDto> getArticlesBySource(String sourceId) {
        log.info("Retrieving articles by source ID: {}", sourceId);
        List<Article> articles = articleRepository.findBySourceId(sourceId);
        return articles.stream()
                .map(this::convertToSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArticleSummaryDto> getArticlesByLabel(String labelId) {
        log.info("Retrieving articles by label ID: {}", labelId);
        List<Article> articles = articleRepository.findByLabelId(labelId);
        return articles.stream()
                .map(this::convertToSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArticleSummaryDto> getArticlesByLabelSlug(String labelSlug) {
        log.info("Retrieving articles by label slug: {}", labelSlug);
        
        Label label = labelRepository.findByUrlSlug(labelSlug)
                .orElseThrow(() -> new ResourceNotFoundException("Label not found with URL slug: " + labelSlug));
        
        return getArticlesByLabel(label.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArticleSummaryDto> getArticlesBySourceSlug(String sourceSlug) {
        log.info("Retrieving articles by source slug: {}", sourceSlug);
        
        Source source = sourceRepository.findByUrlSlug(sourceSlug)
                .orElseThrow(() -> new ResourceNotFoundException("Source not found with URL slug: " + sourceSlug));
        
        return getArticlesBySource(source.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArticleSummaryDto> searchWithFilters(List<String> sourceIds, List<String> labelIds, String searchTerm) {
        log.info("Searching articles with filters - sources: {}, labels: {}, term: {}", sourceIds, labelIds, searchTerm);
        
        List<Article> articles = articleRepository.findWithFilters(sourceIds, labelIds, searchTerm);
        return articles.stream()
                .map(this::convertToSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ArticleStatisticsDto getArticleStatistics() {
        log.info("Generating article statistics");
        
        ArticleStatisticsDto stats = new ArticleStatisticsDto();
        
        // Count total articles
        List<Article> allArticles = articleRepository.findByDeletedFalse();
        stats.setTotalArticles((long) allArticles.size());
        stats.setActiveArticles(articleRepository.countByStatus(Article.ArticleStatus.ACTIVE));
        stats.setDraftArticles(articleRepository.countByStatus(Article.ArticleStatus.DRAFT));
        stats.setDeprecatedArticles(articleRepository.countByStatus(Article.ArticleStatus.DEPRECATED));
        
        // Count total tags (labels used in articles)
        List<ArticleLabel> allArticleLabels = articleLabelRepository.findByDeletedFalse();
        Set<String> uniqueLabelIds = allArticleLabels.stream()
                .map(al -> al.getLabel().getId())
                .collect(Collectors.toSet());
        stats.setTotalTags((long) uniqueLabelIds.size());
        
        // Find most used tag
        Map<String, Long> labelCounts = allArticleLabels.stream()
                .collect(Collectors.groupingBy(
                    al -> al.getLabel().getName(),
                    Collectors.counting()
                ));
        
        Optional<Map.Entry<String, Long>> mostUsedEntry = labelCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue());
        
        if (mostUsedEntry.isPresent()) {
            stats.setMostUsedTag(mostUsedEntry.get().getKey());
        }
        
        // Count articles with and without source
        long articlesWithSource = allArticles.stream()
                .filter(a -> a.getSource() != null)
                .count();
        stats.setArticlesWithSource(articlesWithSource);
        stats.setArticlesWithoutSource(stats.getTotalArticles() - articlesWithSource);
        
        return stats;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkTitleAvailability(String title, String sourceId) {
        log.info("Checking title availability: {} for source: {}", title, sourceId);
        return !articleRepository.existsByTitleAndSourceId(title, sourceId);
    }

    @Override
    public void addLabelToArticle(ArticleLabelCreateDto createDto) {
        log.info("Adding label {} to article {}", createDto.getLabelId(), createDto.getArticleId());
        
        // Validate article
        Article article = articleRepository.findByIdAndNotDeleted(createDto.getArticleId())
                .orElseThrow(() -> new ResourceNotFoundException("Article not found with ID: " + createDto.getArticleId()));
        
        // Validate label
        Label label = labelRepository.findByIdAndNotDeleted(createDto.getLabelId())
                .orElseThrow(() -> new ResourceNotFoundException("Label not found with ID: " + createDto.getLabelId()));
        
        // Check if relationship already exists
        if (articleLabelRepository.existsByArticleIdAndLabelId(createDto.getArticleId(), createDto.getLabelId())) {
            throw new DuplicateResourceException("Label is already associated with this article");
        }
        
        // Create the relationship
        ArticleLabel articleLabel = new ArticleLabel(article, label, createDto.getRelevanceScore(), createDto.getIsPrimary());
        articleLabel.setNotes(createDto.getNotes());
        
        articleLabelRepository.save(articleLabel);
        log.info("Successfully added label to article");
    }

    @Override
    public void addLabelsToArticle(ArticleLabelBulkCreateDto bulkCreateDto) {
        log.info("Adding {} labels to articles in bulk", bulkCreateDto.getArticleLabels().size());
        
        for (ArticleLabelCreateDto createDto : bulkCreateDto.getArticleLabels()) {
            addLabelToArticle(createDto);
        }
        
        log.info("Successfully added labels to articles in bulk");
    }

    @Override
    public void removeLabelFromArticle(String articleId, String labelId) {
        log.info("Removing label {} from article {}", labelId, articleId);
        
        ArticleLabel articleLabel = articleLabelRepository.findByArticleIdAndLabelId(articleId, labelId)
                .orElseThrow(() -> new ResourceNotFoundException("Article-label relationship not found"));
        
        articleLabel.setDeleted(true);
        articleLabelRepository.save(articleLabel);
        
        log.info("Successfully removed label from article");
    }

    @Override
    @Transactional(readOnly = true)
    public String getArticleContent(String id) throws Exception {
        log.info("Fetching content for article ID: {}", id);
        
        // First get the article to retrieve the file path
        Article article = articleRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found with ID: " + id));
        
        String filePath = article.getFilePath();
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalStateException("No file path found for article ID: " + id);
        }
        
        return readFileContent(filePath);
    }

    @Override
    @Transactional(readOnly = true)
    public String getArticleContentByUrlSlug(String urlSlug) throws Exception {
        log.info("Fetching content for article URL slug: {}", urlSlug);
        
        Article article = articleRepository.findByUrlSlug(urlSlug)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found with URL slug: " + urlSlug));
        
        return getArticleContent(article.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArticleLabelReferenceDto> getArticleLabels(String articleId) {
        log.info("Retrieving labels for article ID: {}", articleId);
        
        List<ArticleLabel> articleLabels = articleLabelRepository.findByArticleIdWithLabelDetails(articleId);
        return articleLabels.stream()
                .map(this::convertToArticleLabelReferenceDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArticleLabelReferenceDto> getPrimaryArticleLabels(String articleId) {
        log.info("Retrieving primary labels for article ID: {}", articleId);
        
        List<ArticleLabel> primaryLabels = articleLabelRepository.findPrimaryLabelsByArticleId(articleId);
        return primaryLabels.stream()
                .map(this::convertToArticleLabelReferenceDto)
                .collect(Collectors.toList());
    }

    // Helper methods
    private String generateUniqueSlug(String baseSlug) {
        return generateUniqueSlug(baseSlug, null);
    }

    private String generateUniqueSlug(String baseSlug, String excludeId) {
        String uniqueSlug = baseSlug;
        int counter = 1;
        
        while (true) {
            boolean exists;
            if (excludeId != null) {
                exists = articleRepository.existsByUrlSlugAndIdNot(uniqueSlug, excludeId);
            } else {
                exists = articleRepository.existsByUrlSlug(uniqueSlug);
            }
            
            if (!exists) {
                break;
            }
            
            uniqueSlug = baseSlug + "-" + counter;
            counter++;
        }
        
        return uniqueSlug;
    }

    private String readFileContent(String filePath) throws Exception {
        try {
            // Remove "src/main/resources/" prefix if present, as ClassPathResource looks in classpath
            String resourcePath = filePath;
            if (resourcePath.startsWith("src/main/resources/")) {
                resourcePath = resourcePath.substring("src/main/resources/".length());
            }
            
            // Use ClassPathResource to read from resources folder
            ClassPathResource resource = new ClassPathResource(resourcePath);
            if (!resource.exists()) {
                throw new FileNotFoundException("Article file not found: " + filePath);
            }
            
            try (InputStream inputStream = resource.getInputStream()) {
                return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            log.error("Error reading article content from file: {}", filePath, e);
            throw new Exception("Failed to read article content: " + e.getMessage(), e);
        }
    }

    // Conversion methods
    private ArticleDto convertToDto(Article article) {
        ArticleDto dto = new ArticleDto();
        dto.setId(article.getId());
        dto.setTitle(article.getTitle());
        dto.setShortDescription(article.getShortDescription());
        
        if (article.getSource() != null) {
            SourceSummaryDto sourceDto = new SourceSummaryDto();
            sourceDto.setName(article.getSource().getName());
            sourceDto.setQuestionUri("/api/v1/articles/source/" + article.getSource().getUrlSlug());
            dto.setSource(sourceDto);
        }
        
        dto.setOriginalUrl(article.getOriginalUrl());
        dto.setStatus(article.getStatus().toString());
        dto.setUri("/api/v1/articles/" + article.getUrlSlug());
        
        // Use URL slug if available, otherwise fallback to ID for content URL
        String identifier = article.getUrlSlug() != null ? article.getUrlSlug() : article.getId();
        dto.setContentUrl("/api/v1/articles/" + identifier + "/content");
        
        if (article.getCreatedByUser() != null) {
            UserDto userDto = new UserDto();
            userDto.setId(article.getCreatedByUser().getId());
            userDto.setUsername(article.getCreatedByUser().getUsername());
            userDto.setEmail(article.getCreatedByUser().getEmail());
            dto.setCreatedByUser(userDto);
        }
        
        dto.setVersion(article.getVersion());
        dto.setCreatedAt(article.getCreatedAt());
        dto.setUpdatedAt(article.getUpdatedAt());

        // Populate associated labels/tags
        if (article.getArticleLabels() != null && !article.getArticleLabels().isEmpty()) {
            List<LabelSummaryDto> tags = article.getArticleLabels().stream()
                    .filter(al -> !al.getDeleted())
                    .map(al -> {
                        LabelSummaryDto tagDto = new LabelSummaryDto();
                        tagDto.setId(al.getLabel().getId());
                        tagDto.setName(al.getLabel().getName());
                        tagDto.setCategoryCode(al.getLabel().getCategory().getCode());
                        tagDto.setQuestionUri("/api/v1/articles/label/" + al.getLabel().getUrlSlug());
                        return tagDto;
                    })
                    .toList();
            dto.setTags(tags);
        }

        return dto;
    }

    private ArticleSummaryDto convertToSummaryDto(Article article) {
        ArticleSummaryDto dto = new ArticleSummaryDto();
        dto.setId(article.getId());
        dto.setTitle(article.getTitle());
        dto.setShortDescription(article.getShortDescription());
        
        if (article.getSource() != null) {
            dto.setSourceName(article.getSource().getName());
        }
        dto.setStatus(article.getStatus().toString());
        dto.setUri("/api/v1/articles/" + article.getUrlSlug());
        
        // Use URL slug if available, otherwise fallback to ID for content URL
        String identifier = article.getUrlSlug() != null ? article.getUrlSlug() : article.getId();
        dto.setContentUrl("/api/v1/articles/" + identifier + "/content");
        
        dto.setCreatedAt(article.getCreatedAt());

        // Get primary tags for summary
        if (article.getArticleLabels() != null && !article.getArticleLabels().isEmpty()) {
            List<LabelSummaryDto> primaryTags = article.getArticleLabels().stream()
                    .filter(al -> !al.getDeleted() && (al.getIsPrimary() || al.getRelevanceScore() >= 8))
                    .sorted((al1, al2) -> {
                        // Sort by primary first, then by relevance score descending
                        if (al1.getIsPrimary() && !al2.getIsPrimary()) return -1;
                        if (!al1.getIsPrimary() && al2.getIsPrimary()) return 1;
                        return al2.getRelevanceScore().compareTo(al1.getRelevanceScore());
                    })
                    .limit(5) // Limit to top 5 tags
                    .map(al -> {
                        LabelSummaryDto tagDto = new LabelSummaryDto();
                        tagDto.setId(al.getLabel().getId());
                        tagDto.setName(al.getLabel().getName());
                        tagDto.setCategoryCode(al.getLabel().getCategory().getCode());
                        tagDto.setQuestionUri("/api/v1/articles/label/" + al.getLabel().getUrlSlug());
                        return tagDto;
                    })
                    .toList();
            dto.setPrimaryTags(primaryTags);
        }

        return dto;
    }

    private ArticleLabelReferenceDto convertToArticleLabelReferenceDto(ArticleLabel articleLabel) {
        ArticleLabelReferenceDto dto = new ArticleLabelReferenceDto();
        dto.setId(articleLabel.getId());
        dto.setArticleId(articleLabel.getArticle().getId());
        dto.setRelevanceScore(articleLabel.getRelevanceScore());
        dto.setIsPrimary(articleLabel.getIsPrimary());
        dto.setNotes(articleLabel.getNotes());
        
        if (articleLabel.getLabel() != null) {
            LabelSummaryDto labelDto = new LabelSummaryDto();
            labelDto.setId(articleLabel.getLabel().getId());
            labelDto.setName(articleLabel.getLabel().getName());
            labelDto.setCategoryCode(articleLabel.getLabel().getCategory().getCode());
            labelDto.setQuestionUri("/api/v1/articles/label/" + articleLabel.getLabel().getUrlSlug());
            dto.setLabel(labelDto);
        }
        
        return dto;
    }
}