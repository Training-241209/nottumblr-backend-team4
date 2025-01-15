package com.team4.nottumblr.dto;

public class ReblogsDTO {
    private int reblogId;
    private String comment;
    private String rebloggedAt;
    private String bloggerUsername;
    private String bloggerProfilePictureUrl; 
    private String originalPostContent;
    private String originalPostUsername;
    private String originalPostProfilePictureUrl; 
    private String originalPostMediaUrl; 

    public ReblogsDTO(
            int reblogId, 
            String comment, 
            String rebloggedAt, 
            String bloggerUsername, 
            String bloggerProfilePictureUrl, 
            String originalPostContent, 
            String originalPostUsername, 
            String originalPostProfilePictureUrl,
            String originalPostMediaUrl) {
        this.reblogId = reblogId;
        this.comment = comment;
        this.rebloggedAt = rebloggedAt;
        this.bloggerUsername = bloggerUsername;
        this.bloggerProfilePictureUrl = bloggerProfilePictureUrl;
        this.originalPostContent = originalPostContent;
        this.originalPostUsername = originalPostUsername;
        this.originalPostProfilePictureUrl = originalPostProfilePictureUrl;
        this.originalPostMediaUrl = originalPostMediaUrl;
    }

    public int getReblogId() {
        return reblogId;
    }

    public void setReblogId(int reblogId) {
        this.reblogId = reblogId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getRebloggedAt() {
        return rebloggedAt;
    }

    public void setRebloggedAt(String rebloggedAt) {
        this.rebloggedAt = rebloggedAt;
    }

    public String getBloggerUsername() {
        return bloggerUsername;
    }

    public void setBloggerUsername(String bloggerUsername) {
        this.bloggerUsername = bloggerUsername;
    }

    public String getBloggerProfilePictureUrl() {
        return bloggerProfilePictureUrl;
    }

    public void setBloggerProfilePictureUrl(String bloggerProfilePictureUrl) {
        this.bloggerProfilePictureUrl = bloggerProfilePictureUrl;
    }

    public String getOriginalPostContent() {
        return originalPostContent;
    }

    public void setOriginalPostContent(String originalPostContent) {
        this.originalPostContent = originalPostContent;
    }

    public String getOriginalPostUsername() {
        return originalPostUsername;
    }

    public void setOriginalPostUsername(String originalPostUsername) {
        this.originalPostUsername = originalPostUsername;
    }

    public String getOriginalPostProfilePictureUrl() {
        return originalPostProfilePictureUrl;
    }

    public void setOriginalPostProfilePictureUrl(String originalPostProfilePictureUrl) {
        this.originalPostProfilePictureUrl = originalPostProfilePictureUrl;
    }

    public String getOriginalPostMediaUrl() {
        return originalPostMediaUrl;
    }

    public void setOriginalPostMediaUrl(String originalPostMediaUrl) {
        this.originalPostMediaUrl = originalPostMediaUrl;
    }
}
