package com.example.booktalk.data.remote;

import com.example.booktalk.BuildConfig;
import com.example.booktalk.data.model.Book;
import java.util.ArrayList;
import java.util.List;

/**
 * Mapper to convert Google Books API DTOs to Book models
 */
public class BookMapper {
    
    /**
     * Convert VolumeItem to Book
     */
    public static Book toBook(VolumeItem item) {
        if (item == null || item.getVolumeInfo() == null) {
            return null;
        }
        
        VolumeInfo info = item.getVolumeInfo();
        String bookId = item.getId();
        String title = info.getTitle() != null ? info.getTitle() : "Unknown Title";
        
        // Join authors
        String author = "Unknown Author";
        if (info.getAuthors() != null && !info.getAuthors().isEmpty()) {
            author = String.join(", ", info.getAuthors());
        }
        
        // Get cover URL (prefer thumbnail, fallback to smallThumbnail)
        String coverUrl = null;
        if (info.getImageLinks() != null) {
            coverUrl = info.getImageLinks().getThumbnail();
            if (coverUrl == null) {
                coverUrl = info.getImageLinks().getSmallThumbnail();
            }
            // Replace http with https for better security
            if (coverUrl != null && coverUrl.startsWith("http://")) {
                coverUrl = coverUrl.replace("http://", "https://");
            }
        }
        
        String description = info.getDescription();
        
        Book book = new Book(bookId, title, author, coverUrl, description);
        book.setPublishedDate(info.getPublishedDate());
        book.setRatingsCount(info.getRatingsCount());
        book.setAverageRating(info.getAverageRating());
        
        return book;
    }
    
    /**
     * Convert list of VolumeItems to list of Books
     */
    public static List<Book> toBookList(List<VolumeItem> items) {
        List<Book> books = new ArrayList<>();
        if (items != null) {
            for (VolumeItem item : items) {
                Book book = toBook(item);
                if (book != null) {
                    books.add(book);
                }
            }
        }
        return books;
    }
}

