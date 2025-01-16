package com.team4.nottumblr.dto;

public class TrendingPostsDTO {
    private int postId;
    private String content;
    private String username;

    private String profilePictureUrl;
    private String mediaUrl;
    private String mediaType;

    private int likeCount;
    private int commentCount;
    private int reblogCount;
    private int totalInteractions;

    public TrendingPostsDTO(int postId, String content, String username, int likeCount, int commentCount, int reblogCount, int totalInteractions, String profilePictureUrl, String mediaUrl, String mediaType) {
        this.postId = postId;
        this.content = content;
        this.username = username;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.reblogCount = reblogCount;
        this.totalInteractions = totalInteractions;
        this.profilePictureUrl = profilePictureUrl;
        this.mediaUrl = mediaUrl;
        this.mediaType = mediaType;
    }

    // Getters and Setters
    
    public TrendingPostsDTO() {
        
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }
    
    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
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

    public int getPostId() {
        return postId;
    }
    
    public void setPostId(int postId) {
        this.postId = postId;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public int getLikeCount() {
        return likeCount;
    }
    
    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }
    
    public int getCommentCount() {
        return commentCount;
    }
    
    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
    
    public int getReblogCount() {
        return reblogCount;
    }
    
    public void setReblogCount(int reblogCount) {
        this.reblogCount = reblogCount;
    }
    
    public int getTotalInteractions() {
        return totalInteractions;
    }
    
    public void setTotalInteractions(int totalInteractions) {
        this.totalInteractions = totalInteractions;
    }
}

