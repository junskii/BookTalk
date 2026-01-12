package com.example.booktalk.data.model;

import java.util.List;

/**
 * Category model with associated books
 */
public class Category {
    private int categoryId;
    private String name;
    private String queryHint;
    private long fetchedAt;
    private List<Book> books;
    
    public Category() {
    }
    
    public Category(String name, String queryHint) {
        this.name = name;
        this.queryHint = queryHint;
        this.fetchedAt = 0;
    }
    
    // Getters and Setters
    public int getCategoryId() {
        return categoryId;
    }
    
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getQueryHint() {
        return queryHint;
    }
    
    public void setQueryHint(String queryHint) {
        this.queryHint = queryHint;
    }
    
    public long getFetchedAt() {
        return fetchedAt;
    }
    
    public void setFetchedAt(long fetchedAt) {
        this.fetchedAt = fetchedAt;
    }
    
    public List<Book> getBooks() {
        return books;
    }
    
    public void setBooks(List<Book> books) {
        this.books = books;
    }
}

