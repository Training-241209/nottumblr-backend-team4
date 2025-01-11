package com.team4.nottumblr.dto;

public class LikesDTO {
    private int likeId;
    private String username;
    private int postId;

    public LikesDTO(int likeId, String username, int postId) {
        this.likeId = likeId;
        this.username = username;
        this.postId = postId;
    }

    // Getters and setters
    public int getLikeId() {
        return likeId;
    }

    public void setLikeId(int likeId) {
        this.likeId = likeId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }
}

