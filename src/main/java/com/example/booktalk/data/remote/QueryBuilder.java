package com.example.booktalk.data.remote;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Centralized query builder for Google Books API
 * Ensures consistent parameters and proper query formatting
 */
public class QueryBuilder {
    
    // Default configuration
    public static final String DEFAULT_LANG = "en";
    public static final String DEFAULT_COUNTRY = "US";
    public static final int DEFAULT_MAX_RESULTS = 40;
    public static final String DEFAULT_PRINT_TYPE = "books";
    public static final String DEFAULT_ORDER_BY = "relevance";
    
    /**
     * Build query for home category
     * @param categoryQueryHint Subject query (e.g., "subject:romance")
     * @return Formatted query string
     */
    public static String buildHomeCategoryQuery(String categoryQueryHint) {
        if (categoryQueryHint == null || categoryQueryHint.trim().isEmpty()) {
            return "";
        }
        return categoryQueryHint.trim();
    }
    
    /**
     * Build primary strict search query using intitle: operator
     * This gives more accurate results for specific book searches
     * @param userText User's search input
     * @return Formatted query string with intitle: operator
     */
    public static String buildSearchPrimaryQuery(String userText) {
        if (userText == null || userText.trim().isEmpty()) {
            return "";
        }
        
        String trimmed = userText.trim();
        // Escape quotes and special characters
        String escaped = trimmed.replace("\"", "\\\"");
        
        // Use intitle: for strict title matching
        return "intitle:\"" + escaped + "\"";
    }
    
    /**
     * Build enhanced search query with both title and author
     * @param userText User's search input
     * @param authorName Optional author name (can be null)
     * @return Formatted query string
     */
    public static String buildSearchEnhancedQuery(String userText, String authorName) {
        if (userText == null || userText.trim().isEmpty()) {
            return "";
        }
        
        String trimmed = userText.trim();
        String escaped = trimmed.replace("\"", "\\\"");
        
        if (authorName != null && !authorName.trim().isEmpty()) {
            String authorEscaped = authorName.trim().replace("\"", "\\\"");
            return "intitle:\"" + escaped + "\" inauthor:\"" + authorEscaped + "\"";
        }
        
        return "intitle:\"" + escaped + "\"";
    }
    
    /**
     * Build fallback search query (less strict, uses raw text)
     * Used when primary query returns too few results
     * @param userText User's search input
     * @return Formatted query string (raw text, not URL encoded - Retrofit handles encoding)
     */
    public static String buildSearchFallbackQuery(String userText) {
        if (userText == null || userText.trim().isEmpty()) {
            return "";
        }
        
        // Return raw text - Retrofit will URL encode it automatically
        return userText.trim();
    }
    
    /**
     * Get default language restriction
     */
    public static String getDefaultLang() {
        return DEFAULT_LANG;
    }
    
    /**
     * Get default max results
     */
    public static int getDefaultMaxResults() {
        return DEFAULT_MAX_RESULTS;
    }
    
    /**
     * Get default print type
     */
    public static String getDefaultPrintType() {
        return DEFAULT_PRINT_TYPE;
    }
    
    /**
     * Get default order by
     */
    public static String getDefaultOrderBy() {
        return DEFAULT_ORDER_BY;
    }
    
    /**
     * Get default country
     */
    public static String getDefaultCountry() {
        return DEFAULT_COUNTRY;
    }
}

