package com.example.tailgatehub.models;

public class User {
    private String userId;
    private String username;
    private String email;
    private String profileImageUrl;

    // Constructor
    public User(String userId, String username, String email, String profileImageUrl) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }
    public void setId(String userId) {
        this.userId = this.userId;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }
    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}