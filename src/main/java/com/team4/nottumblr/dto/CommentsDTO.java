package com.team4.nottumblr.dto;

import java.time.LocalDateTime;

public class CommentsDTO {
    private int commentId;
    private String content;
    private LocalDateTime createdAt;
    private String bloggerUsername;
    private int postId;

    // Constructor
    public CommentsDTO(int commentId, String content, LocalDateTime createdAt, String bloggerUsername, int postId) {
        this.commentId = commentId;
        this.content = content;
        this.createdAt = createdAt;
        this.bloggerUsername = bloggerUsername;
        this.postId = postId;
    }

    // Getters
    public int getCommentId() {
        return commentId;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getBloggerUsername() {
        return bloggerUsername;
    }

    public int getPostId() {
        return postId;
    }

    // Setters
    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setBloggerUsername(String bloggerUsername) {
        this.bloggerUsername = bloggerUsername;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }
}
