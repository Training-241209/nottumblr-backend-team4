package com.team4.nottumblr.dto;

public class BloggersFollowersDTO {
    private long bloggerId;
    private String username;
    private String profilePictureUrl;
    private int followerCount;

    public BloggersFollowersDTO(long bloggerId, String username, String profilePictureUrl, int followerCount) {
        this.bloggerId = bloggerId;
        this.username = username;
        this.profilePictureUrl = profilePictureUrl;
        this.followerCount = followerCount;
    }

    // Getter and Setter for bloggerId
    public long getBloggerId() {
        return bloggerId;
    }

    public void setBloggerId(long bloggerId) {
        this.bloggerId = bloggerId;
    }

    // Getter and Setter for username
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Getter and Setter for profilePictureUrl
    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    // Getter and Setter for followerCount
    public int getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }
}
