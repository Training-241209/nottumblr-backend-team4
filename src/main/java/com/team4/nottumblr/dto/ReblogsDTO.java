package com.team4.nottumblr.dto;

public class ReblogsDTO {
    private int reblogId;
    private String comment;
    private String rebloggedAt;
    private String bloggerUsername;
    private String originalPostContent;
    private String originalPostUsername;

    public ReblogsDTO(int reblogId, String comment, String rebloggedAt, 
                      String bloggerUsername, String originalPostContent, 
                      String originalPostUsername) {
        this.reblogId = reblogId;
        this.comment = comment;
        this.rebloggedAt = rebloggedAt;
        this.bloggerUsername = bloggerUsername;
        this.originalPostContent = originalPostContent;
        this.originalPostUsername = originalPostUsername;
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
}