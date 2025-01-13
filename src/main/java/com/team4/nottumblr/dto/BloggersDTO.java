package com.team4.nottumblr.dto;

public class BloggersDTO {

    private long bloggerId;
    private String username;
    private String email;
    private String roleName;
    private String firstName;
    private String lastName;

    public BloggersDTO(long bloggerId, String username, String email, String roleName, String firstName, String lastName) {
        this.bloggerId = bloggerId;
        this.username = username;
        this.email = email;
        this.roleName = roleName;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
}
