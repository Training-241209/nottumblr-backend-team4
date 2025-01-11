package com.team4.nottumblr.dto;

import java.time.LocalDateTime;


public class PostsDTO {
    private int postId;
    private String username;
    private String content;
    private String mediaUrl;
    private String mediaType;
    private LocalDateTime createdAt;
    private int blogId; 

    // Constructors, getters, setters
    public PostsDTO(int postId, String username, String content, String mediaUrl, String mediaType, LocalDateTime createdAt, int blogId) {
        this.postId = postId;
        this.username = username;
        this.content = content;
        this.mediaUrl = mediaUrl;
        this.mediaType = mediaType;
        this.createdAt = createdAt;
        this.blogId = blogId;
    }

    public PostsDTO() {
    }

    public int getPostId() {
        return postId;
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

    public String getMediaUrl() {
        return mediaUrl;
    }

    public String getMediaType() {
        return mediaType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public int getBlogId() {
        return blogId;
    }

    public void setBlogId(int blogId) {
        this.blogId = blogId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}

