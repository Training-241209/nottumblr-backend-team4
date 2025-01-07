package com.team4.nottumblr.model;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;


@Entity
public class Bloggers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long blogger_id;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Roles role;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String profile_picture_url;

    @OneToMany(mappedBy = "blogger", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Blogs> blogs;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime created_at;

    public Bloggers(long blogger_id, Roles role, String username, String email, String password, String profile_picture_url, LocalDateTime created_at) {
        this.blogger_id = blogger_id;
        this.role = role;
        this.username = username;
        this.email = email;
        this.password = password;
        this.profile_picture_url = profile_picture_url;
        this.created_at = created_at;
    }

    public Bloggers() {}

    //JWT Constructor
    public Bloggers(long blogger_id, String email, Roles role) {
        this.blogger_id = blogger_id;
        this.email = email;
        this.role = role;
    }

    public List<Blogs> getBlogs() {
        return blogs;
    }

    public void setBlogs(List<Blogs> blogs) {
        this.blogs = blogs;
    }

    public long getBlogger_id() {
        return blogger_id;
    }

    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
    }

    public String getUsername() { 
        return username;    
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getProfile_picture_url() {
        return profile_picture_url;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setBlogger_id(long blogger_id) {
        this.blogger_id = blogger_id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setProfile_picture_url(String profile_picture_url) {
        this.profile_picture_url = profile_picture_url;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }
}
