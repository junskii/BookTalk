package com.example.booktalk.data.remote;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * DTO for volume information
 */
public class VolumeInfo {
    @SerializedName("title")
    private String title;
    
    @SerializedName("authors")
    private List<String> authors;
    
    @SerializedName("description")
    private String description;
    
    @SerializedName("imageLinks")
    private ImageLinks imageLinks;
    
    @SerializedName("publishedDate")
    private String publishedDate;
    
    @SerializedName("ratingsCount")
    private Integer ratingsCount;
    
    @SerializedName("averageRating")
    private Double averageRating;
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public List<String> getAuthors() {
        return authors;
    }
    
    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public ImageLinks getImageLinks() {
        return imageLinks;
    }
    
    public void setImageLinks(ImageLinks imageLinks) {
        this.imageLinks = imageLinks;
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

