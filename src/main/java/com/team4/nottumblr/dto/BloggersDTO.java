package com.team4.nottumblr.dto;

public class BloggersDTO {

    private long bloggerId;
    private String username;
    private String roleName;

    public long getBloggerId() {
        return bloggerId;
    }

    public String getUsername() {
        return username;
    }

    public void setBloggerId(long bloggerId) {
        this.bloggerId = bloggerId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    
}
