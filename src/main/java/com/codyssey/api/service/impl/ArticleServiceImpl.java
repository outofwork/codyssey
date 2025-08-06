package com.codyssey.api.service.impl;

import com.codyssey.api.dto.article.ArticleDto;
import com.codyssey.api.dto.article.ArticleSummaryDto;
import com.codyssey.api.exception.ResourceNotFoundException;
import com.codyssey.api.model.Article;
import com.codyssey.api.service.ArticleService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of ArticleService
 * <p>
 * Manages file-based articles with metadata indexing for efficient operations.
 */
@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {

    private final ObjectMapper objectMapper;
    private List<Article> articlesIndex = new ArrayList<>();
    
    private static final String ARTICLES_INDEX_PATH = "articles/metadata/articles-index.json";
    private static final String ARTICLES_BASE_PATH = "articles/";

    public ArticleServiceImpl() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @PostConstruct
    public void initializeIndex() {
        refreshIndex();
    }

    @Override
    public void refreshIndex() {
        try {
            log.info("Loading articles index from: {}", ARTICLES_INDEX_PATH);
            ClassPathResource resource = new ClassPathResource(ARTICLES_INDEX_PATH);
            
            if (resource.exists()) {
                try (InputStream inputStream = resource.getInputStream()) {
                    articlesIndex = objectMapper.readValue(inputStream, new TypeReference<List<Article>>() {});
                    log.info("Loaded {} articles from index", articlesIndex.size());
                }
            } else {
                log.warn("Articles index file not found: {}", ARTICLES_INDEX_PATH);
                articlesIndex = new ArrayList<>();
            }
        } catch (IOException e) {
            log.error("Error loading articles index", e);
            articlesIndex = new ArrayList<>();
        }
    }

    @Override
    public List<ArticleSummaryDto> getAllArticles() {
        return articlesIndex.stream()
                .filter(Article::getPublished)
                .map(this::convertToSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ArticleSummaryDto> getArticlesByCategory(String category) {
        return articlesIndex.stream()
                .filter(Article::getPublished)
                .filter(article -> category.equalsIgnoreCase(article.getCategory()))
                .map(this::convertToSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ArticleDto> getArticleById(String id) {
        return articlesIndex.stream()
                .filter(Article::getPublished)
                .filter(article -> id.equals(article.getId()))
                .findFirst()
                .map(this::loadArticleContent);
    }

    @Override
    public Optional<ArticleDto> getArticleBySlug(String slug) {
        return articlesIndex.stream()
                .filter(Article::getPublished)
                .filter(article -> slug.equals(article.getSlug()))
                .findFirst()
                .map(this::loadArticleContent);
    }

    @Override
    public List<ArticleSummaryDto> searchArticles(String query) {
        String lowerQuery = query.toLowerCase();
        return articlesIndex.stream()
                .filter(Article::getPublished)
                .filter(article -> 
                    article.getTitle().toLowerCase().contains(lowerQuery) ||
                    article.getDescription().toLowerCase().contains(lowerQuery) ||
                    article.getTags().stream().anyMatch(tag -> tag.toLowerCase().contains(lowerQuery))
                )
                .map(this::convertToSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ArticleSummaryDto> getArticlesByTags(List<String> tags) {
        Set<String> tagSet = tags.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
                
        return articlesIndex.stream()
                .filter(Article::getPublished)
                .filter(article -> article.getTags().stream()
                        .anyMatch(tag -> tagSet.contains(tag.toLowerCase())))
                .map(this::convertToSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ArticleSummaryDto> getArticlesByDifficulty(String difficulty) {
        return articlesIndex.stream()
                .filter(Article::getPublished)
                .filter(article -> difficulty.equalsIgnoreCase(article.getDifficulty()))
                .map(this::convertToSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ArticleSummaryDto> getRelatedArticles(String articleId) {
        Optional<Article> currentArticle = articlesIndex.stream()
                .filter(article -> articleId.equals(article.getId()))
                .findFirst();
                
        if (!currentArticle.isPresent()) {
            return new ArrayList<>();
        }
        
        List<String> relatedIds = currentArticle.get().getRelatedTopics();
        if (relatedIds == null || relatedIds.isEmpty()) {
            return new ArrayList<>();
        }
        
        return articlesIndex.stream()
                .filter(Article::getPublished)
                .filter(article -> relatedIds.contains(article.getId()))
                .map(this::convertToSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getAvailableCategories() {
        return articlesIndex.stream()
                .filter(Article::getPublished)
                .map(Article::getCategory)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getAvailableTags() {
        return articlesIndex.stream()
                .filter(Article::getPublished)
                .flatMap(article -> article.getTags().stream())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    private ArticleDto loadArticleContent(Article article) {
        try {
            String content = loadMarkdownContent(article.getFilePath());
            
            ArticleDto dto = convertToDto(article);
            dto.setContent(content);
            // TODO: Add HTML conversion if needed
            // dto.setHtmlContent(convertMarkdownToHtml(content));
            
            return dto;
        } catch (Exception e) {
            log.error("Error loading content for article: {}", article.getId(), e);
            throw new ResourceNotFoundException("Article content not found: " + article.getId());
        }
    }

    private String loadMarkdownContent(String filePath) throws IOException {
        String fullPath = ARTICLES_BASE_PATH + filePath;
        ClassPathResource resource = new ClassPathResource(fullPath);
        
        if (!resource.exists()) {
            throw new IOException("Article file not found: " + fullPath);
        }
        
        try (InputStream inputStream = resource.getInputStream()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    private ArticleDto convertToDto(Article article) {
        ArticleDto dto = new ArticleDto();
        dto.setId(article.getId());
        dto.setTitle(article.getTitle());
        dto.setDescription(article.getDescription());
        dto.setCategory(article.getCategory());
        dto.setSubcategory(article.getSubcategory());
        dto.setSlug(article.getSlug());
        dto.setTags(article.getTags());
        dto.setDifficulty(article.getDifficulty());
        dto.setReadTime(article.getReadTime());
        dto.setAuthor(article.getAuthor());
        dto.setLastModified(article.getLastModified());
        dto.setMetaDescription(article.getMetaDescription());
        dto.setPrerequisites(article.getPrerequisites());
        dto.setRelatedTopics(article.getRelatedTopics());
        return dto;
    }

    private ArticleSummaryDto convertToSummaryDto(Article article) {
        ArticleSummaryDto dto = new ArticleSummaryDto();
        dto.setId(article.getId());
        dto.setTitle(article.getTitle());
        dto.setDescription(article.getDescription());
        dto.setCategory(article.getCategory());
        dto.setSubcategory(article.getSubcategory());
        dto.setSlug(article.getSlug());
        dto.setTags(article.getTags());
        dto.setDifficulty(article.getDifficulty());
        dto.setReadTime(article.getReadTime());
        dto.setAuthor(article.getAuthor());
        dto.setLastModified(article.getLastModified());
        dto.setMetaDescription(article.getMetaDescription());
        return dto;
    }
}