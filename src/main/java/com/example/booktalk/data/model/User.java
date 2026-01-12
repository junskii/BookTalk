package com.example.booktalk.data.model;

/**
 * User model
 */
public class User {
    private int userId;
    private String name;
    private String username;
    private String password;
    private String avatarUri;
    private long createdAt;
    
    public User() {
    }
    
    public User(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.createdAt = System.currentTimeMillis();
    }
    
    // Getters and Setters
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getAvatarUri() {
        return avatarUri;
    }
    
    public void setAvatarUri(String avatarUri) {
        this.avatarUri = avatarUri;
    }
    
    public long getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}

