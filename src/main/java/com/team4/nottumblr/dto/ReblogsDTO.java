package com.team4.nottumblr.dto;

public class ReblogsDTO {
    private int reblogId;
    private String comment;
    private String rebloggedAt;
    private String bloggerUsername;
    private String blogTitle;

    public ReblogsDTO(int reblogId, String comment, String rebloggedAt, String bloggerUsername, String blogTitle) {
        this.reblogId = reblogId;
        this.comment = comment;
        this.rebloggedAt = rebloggedAt;
        this.bloggerUsername = bloggerUsername;
        this.blogTitle = blogTitle;
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

    public String getBlogTitle() {
        return blogTitle;
    }

    public void setBlogTitle(String blogTitle) {
        this.blogTitle = blogTitle;
    }
}
