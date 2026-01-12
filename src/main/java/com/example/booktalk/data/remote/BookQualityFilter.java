package com.example.booktalk.data.remote;

import com.example.booktalk.data.model.Book;
import com.example.booktalk.util.DateParser;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Filters and ranks books by quality metrics
 * Uses ratingsCount, averageRating, and publishedDate for ranking
 */
public class BookQualityFilter {
    
    /**
     * Filter out low-quality books and rank by popularity metrics
     * @param books List of books to filter and rank
     * @param maxResults Maximum number of results to return
     * @param minYear Optional minimum year filter (e.g., currentYear - 10). Use -1 to disable
     * @return Filtered and ranked list of books
     */
    public static List<Book> filterAndRank(List<Book> books, int maxResults, int minYear) {
        if (books == null || books.isEmpty()) {
            return new ArrayList<>();
        }
        
        List<Book> filtered = new ArrayList<>();
        
        // Step 1: Filter out low-quality items
        for (Book book : books) {
            if (isQualityBook(book, minYear)) {
                filtered.add(book);
            }
        }
        
        // Step 2: Rank by quality metrics
        Collections.sort(filtered, new BookQualityComparator());
        
        // Step 3: Take top N
        if (filtered.size() > maxResults) {
            return filtered.subList(0, maxResults);
        }
        
        return filtered;
    }
    
    /**
     * STRICT: Check if book has ALL required fields (title, author, thumbnail, description)
     * This is the new strict completeness requirement
     */
    public static boolean isCompleteBook(Book book) {
        // MUST have title (non-empty)
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            return false;
        }
        
        // MUST have author(s) (non-empty)
        if (book.getAuthor() == null || book.getAuthor().trim().isEmpty() || 
            book.getAuthor().equals("Unknown Author")) {
            return false;
        }
        
        // MUST have cover thumbnail URL (thumbnail or smallThumbnail)
        if (book.getCoverUrl() == null || book.getCoverUrl().trim().isEmpty()) {
            return false;
        }
        
        // MUST have description (non-empty)
        if (book.getDescription() == null || book.getDescription().trim().isEmpty()) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Check if book meets quality criteria (for initial filtering before completeness check)
     * Filters out non-English books and basic quality issues
     */
    private static boolean isQualityBook(Book book, int minYear) {
        // MUST have title (hard requirement)
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            return false;
        }
        
        // Filter out non-English books (check for CJK characters or other non-ASCII)
        if (!isEnglishText(book.getTitle())) {
            return false;
        }
        
        // Also check author if exists
        if (book.getAuthor() != null && !book.getAuthor().trim().isEmpty()) {
            if (!isEnglishText(book.getAuthor())) {
                return false; // Author is non-English, likely non-English book
            }
        }
        
        // PREFER has authors (soft requirement - filter out if no author AND no cover)
        boolean hasAuthor = book.getAuthor() != null && !book.getAuthor().trim().isEmpty() && 
                           !book.getAuthor().equals("Unknown Author");
        boolean hasCover = book.getCoverUrl() != null && !book.getCoverUrl().isEmpty();
        
        // If no author AND no cover, it's likely low quality - filter out
        if (!hasAuthor && !hasCover) {
            return false;
        }
        
        // Optional: filter by year if specified
        if (minYear > 0 && book.getPublishedDate() != null) {
            int year = DateParser.getYear(book.getPublishedDate());
            if (year != -1 && year < minYear) {
                return false; // Too old
            }
        }
        
        return true;
    }
    
    /**
     * Check if text is English (ASCII characters, basic punctuation)
     * Filters out CJK (Chinese, Japanese, Korean) and other non-English characters
     */
    private static boolean isEnglishText(String text) {
        if (text == null || text.trim().isEmpty()) {
            return false;
        }
        
        // Check for CJK characters (common ranges)
        // Chinese: \u4E00-\u9FFF
        // Japanese: \u3040-\u309F, \u30A0-\u30FF
        // Korean: \uAC00-\uD7AF
        for (char c : text.toCharArray()) {
            // Check for CJK characters
            if ((c >= 0x4E00 && c <= 0x9FFF) ||  // Chinese
                (c >= 0x3040 && c <= 0x309F) ||  // Hiragana
                (c >= 0x30A0 && c <= 0x30FF) ||  // Katakana
                (c >= 0xAC00 && c <= 0xD7AF)) {  // Korean
                return false;
            }
        }
        
        // If text contains mostly ASCII/English characters, consider it English
        // Allow some non-ASCII for accented characters (é, ñ, etc.) but not CJK
        return true;
    }
    
    /**
     * Rank books by ratings (highest-rated first)
     * Priority: ratingsCount DESC, averageRating DESC, publishedDate DESC
     */
    public static List<Book> rankBooksByRatings(List<Book> books) {
        if (books == null || books.isEmpty()) {
            return new ArrayList<>();
        }
        
        List<Book> sorted = new ArrayList<>(books);
        Collections.sort(sorted, new BookQualityComparator());
        return sorted;
    }
    
    /**
     * Comparator for ranking books by quality metrics
     * Priority: ratingsCount DESC, averageRating DESC, publishedDate DESC
     */
    private static class BookQualityComparator implements Comparator<Book> {
        @Override
        public int compare(Book b1, Book b2) {
            // 1. Rank by ratingsCount (higher is better, null treated as 0)
            int r1Count = b1.getRatingsCount() != null ? b1.getRatingsCount() : 0;
            int r2Count = b2.getRatingsCount() != null ? b2.getRatingsCount() : 0;
            int countCompare = Integer.compare(r2Count, r1Count); // DESC
            if (countCompare != 0) {
                return countCompare;
            }
            
            // 2. Rank by averageRating (higher is better, null treated as 0)
            double r1Avg = b1.getAverageRating() != null ? b1.getAverageRating() : 0.0;
            double r2Avg = b2.getAverageRating() != null ? b2.getAverageRating() : 0.0;
            int ratingCompare = Double.compare(r2Avg, r1Avg); // DESC
            if (ratingCompare != 0) {
                return ratingCompare;
            }
            
            // 3. Rank by publishedDate (newer is better, as tiebreaker)
            int y1 = b1.getPublishedDate() != null ? DateParser.getYear(b1.getPublishedDate()) : -1;
            int y2 = b2.getPublishedDate() != null ? DateParser.getYear(b2.getPublishedDate()) : -1;
            if (y1 != -1 && y2 != -1) {
                int yearCompare = Integer.compare(y2, y1); // DESC (newer first)
                if (yearCompare != 0) {
                    return yearCompare;
                }
            }
            
            // 4. Prefer books with cover images
            boolean b1HasCover = b1.getCoverUrl() != null && !b1.getCoverUrl().isEmpty();
            boolean b2HasCover = b2.getCoverUrl() != null && !b2.getCoverUrl().isEmpty();
            if (b1HasCover != b2HasCover) {
                return b2HasCover ? 1 : -1;
            }
            
            // 5. Prefer books with authors
            boolean b1HasAuthor = b1.getAuthor() != null && !b1.getAuthor().trim().isEmpty();
            boolean b2HasAuthor = b2.getAuthor() != null && !b2.getAuthor().trim().isEmpty();
            if (b1HasAuthor != b2HasAuthor) {
                return b2HasAuthor ? 1 : -1;
            }
            
            return 0; // Equal
        }
    }
}

