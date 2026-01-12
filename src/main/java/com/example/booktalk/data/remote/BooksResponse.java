package com.example.booktalk.data.remote;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * DTO for Google Books API search response
 */
public class BooksResponse {
    @SerializedName("items")
    private List<VolumeItem> items;
    
    @SerializedName("totalItems")
    private int totalItems;
    
    public List<VolumeItem> getItems() {
        return items;
    }
    
    public void setItems(List<VolumeItem> items) {
        this.items = items;
    }
    
    public int getTotalItems() {
        return totalItems;
    }
    
    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }
}

