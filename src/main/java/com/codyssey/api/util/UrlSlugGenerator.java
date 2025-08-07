package com.codyssey.api.util;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Utility class for generating URL-friendly slugs from entity names
 * <p>
 * This class provides methods to convert entity names into SEO-friendly URL slugs
 * that can be used instead of IDs in REST API endpoints.
 */
public class UrlSlugGenerator {

    private static final Pattern NON_LATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");
    private static final Pattern EDGES_DASHES = Pattern.compile("(^-|-$)");
    private static final Pattern MULTIPLE_DASHES = Pattern.compile("-{2,}");

    /**
     * Generate a URL-friendly slug from the given text
     * 
     * @param input the input text to convert to a slug
     * @return URL-friendly slug
     */
    public static String generateSlug(String input) {
        if (input == null || input.trim().isEmpty()) {
            return "";
        }

        String slug = input.trim().toLowerCase(Locale.ENGLISH);
        
        // Normalize to remove accents and special characters
        slug = Normalizer.normalize(slug, Normalizer.Form.NFD);
        
        // Replace whitespace with dashes
        slug = WHITESPACE.matcher(slug).replaceAll("-");
        
        // Remove non-latin characters (keep alphanumeric, dashes, underscores)
        slug = NON_LATIN.matcher(slug).replaceAll("");
        
        // Replace multiple consecutive dashes with single dash
        slug = MULTIPLE_DASHES.matcher(slug).replaceAll("-");
        
        // Remove dashes from edges
        slug = EDGES_DASHES.matcher(slug).replaceAll("");
        
        return slug;
    }

    /**
     * Generate a unique slug by appending a counter if necessary
     * 
     * @param baseSlug the base slug to make unique
     * @param existingSlugs set of existing slugs to check against
     * @return unique slug
     */
    public static String generateUniqueSlug(String baseSlug, java.util.Set<String> existingSlugs) {
        if (existingSlugs == null || !existingSlugs.contains(baseSlug)) {
            return baseSlug;
        }

        int counter = 1;
        String uniqueSlug;
        do {
            uniqueSlug = baseSlug + "-" + counter;
            counter++;
        } while (existingSlugs.contains(uniqueSlug));

        return uniqueSlug;
    }

    /**
     * Generate a slug for coding questions combining title and source
     * Format: {title-slug}-{source-code}
     * 
     * @param title the question title
     * @param sourceCode the source platform code (e.g., "LEETCODE")
     * @return formatted question slug
     */
    public static String generateQuestionSlug(String title, String sourceCode) {
        String titleSlug = generateSlug(title);
        String sourceSlug = sourceCode != null ? sourceCode.toLowerCase() : "";
        
        if (titleSlug.isEmpty()) {
            return sourceSlug;
        }
        if (sourceSlug.isEmpty()) {
            return titleSlug;
        }
        
        return titleSlug + "-" + sourceSlug;
    }

    /**
     * Generate a slug for labels using only the label name
     * Format: {label-slug}
     * 
     * @param labelName the label name
     * @param categoryName the category name (kept for compatibility but not used)
     * @return formatted label slug based only on label name
     */
    public static String generateLabelSlug(String labelName, String categoryName) {
        return generateSlug(labelName);
    }

    /**
     * Generate a slug for sources from their name
     * 
     * @param sourceName the source name
     * @return source slug
     */
    public static String generateSourceSlug(String sourceName) {
        return generateSlug(sourceName);
    }

    /**
     * Generate a slug for users from their username
     * 
     * @param username the username
     * @return user slug (cleaned username)
     */
    public static String generateUserSlug(String username) {
        return generateSlug(username);
    }

    /**
     * Generate a slug for label categories from their name
     * 
     * @param categoryName the category name
     * @return category slug
     */
    public static String generateCategorySlug(String categoryName) {
        return generateSlug(categoryName);
    }
}