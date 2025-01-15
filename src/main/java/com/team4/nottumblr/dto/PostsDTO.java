package com.team4.nottumblr.dto;

import java.time.LocalDateTime;

public class PostsDTO {
    private int postId;
    private String username;
    private String content;
    private String mediaUrl;
    private String mediaType;
    private LocalDateTime createdAt;
    private long bloggerId;
    private String profilePictureUrl;

    // Updated constructor to include profilePictureUrl
    public PostsDTO(int postId, String username, String content, String mediaUrl, 
                   String mediaType, LocalDateTime createdAt, long bloggerId, 
                   String profilePictureUrl) {
        this.postId = postId;
        this.username = username;
        this.content = content;
        this.mediaUrl = mediaUrl;
        this.mediaType = mediaType;
        this.createdAt = createdAt;
        this.bloggerId = bloggerId;
        this.profilePictureUrl = profilePictureUrl;
    }

    // No-args constructor
    public PostsDTO() {
    }

    // Existing getters and setters
    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public long getBloggerId() {
        return bloggerId;
    }

    public void setBloggerId(long bloggerId) {
        this.bloggerId = bloggerId;
    }

    // New getter and setter for profilePictureUrl
    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }
}