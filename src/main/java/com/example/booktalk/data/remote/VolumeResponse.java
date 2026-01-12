package com.example.booktalk.data.remote;

import com.google.gson.annotations.SerializedName;

/**
 * DTO for Google Books API single volume response
 */
public class VolumeResponse {
    @SerializedName("id")
    private String id;
    
    @SerializedName("volumeInfo")
    private VolumeInfo volumeInfo;
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public VolumeInfo getVolumeInfo() {
        return volumeInfo;
    }
    
    public void setVolumeInfo(VolumeInfo volumeInfo) {
        this.volumeInfo = volumeInfo;
    }
}

