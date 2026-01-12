package com.example.booktalk.data.model;

/**
 * Book model
 */
public class Book {
    private String bookId;
    private String title;
    private String author;
    private String coverUrl;
    private String description;
    private long fetchedAt;
    private long lastOpenedAt;
    private String publishedDate;
    private Integer ratingsCount;
    private Double averageRating;
    
    public Book() {
    }
    
    public Book(String bookId, String title, String author, String coverUrl, String description) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.coverUrl = coverUrl;
        this.description = description;
        this.fetchedAt = System.currentTimeMillis();
        this.lastOpenedAt = 0;
    }
    
    // Getters and Setters
    public String getBookId() {
        return bookId;
    }
    
    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }
    
    public String getCoverUrl() {
        return coverUrl;
    }
    
    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public long getFetchedAt() {
        return fetchedAt;
    }
    
    public void setFetchedAt(long fetchedAt) {
        this.fetchedAt = fetchedAt;
    }
    
    public long getLastOpenedAt() {
        return lastOpenedAt;
    }
    
    public void setLastOpenedAt(long lastOpenedAt) {
        this.lastOpenedAt = lastOpenedAt;
    }
    
    public String getPublishedDate() {
        return publishedDate;
    }
    
    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }
    
    public Integer getRatingsCount() {
        return ratingsCount;
    }
    
    public void setRatingsCount(Integer ratingsCount) {
        this.ratingsCount = ratingsCount;
    }
    
    public Double getAverageRating() {
        return averageRating;
    }
    
    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }
}

