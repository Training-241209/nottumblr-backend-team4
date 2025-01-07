package com.team4.nottumblr.dto;

public class BloggersDTO {

    private long blogger_id;
    private String username;

    public long getBlogger_id() {
        return blogger_id;
    }

    public String getUsername() {
        return username;
    }

    public void setBlogger_id(long blogger_id) {
        this.blogger_id = blogger_id;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
}
