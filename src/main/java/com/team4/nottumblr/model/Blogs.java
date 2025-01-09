package com.team4.nottumblr.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Blogs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int blogId;

    @Column(unique = true)
    private String title;

    @Column(unique = true)
    private String description;

    @ManyToOne
    @JoinColumn(name = "bloggerId") 
    private Bloggers blogger;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    public Blogs(int blogId, String title, Bloggers blogger, String description, LocalDateTime createdAt) {
        this.blogId = blogId;
        this.title = title;
        this.blogger = blogger;
        this.description = description;
        this.createdAt = createdAt;
    }

    public Blogs() {}

    public int getBlogId() {
        return blogId;
    }

    public void setBlogId(int blogId) {
        this.blogId = blogId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Bloggers getBlogger() {
        return blogger;
    }

    public void setBlogger(Bloggers blogger) {
        this.blogger = blogger;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
