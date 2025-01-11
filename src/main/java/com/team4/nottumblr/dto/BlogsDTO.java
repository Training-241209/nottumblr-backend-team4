package com.team4.nottumblr.dto;

public class BlogsDTO {
    private String title;
    private String description;
    private String username; 

    public BlogsDTO(String title, String description, String username) {
        this.title = title;
        this.description = description;
        this.username = username;
    }

    // Getters and setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
