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
    private int blog_id;

    @Column(nullable = false, unique = true)
    private String title;

    @Column(nullable = false, unique = true)
    private String description;

    @ManyToOne
    @JoinColumn(name = "blogger_id", nullable = false) 
    private Bloggers blogger;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime created_at;

    public Blogs(int blog_id, String title, Bloggers blogger, String description, LocalDateTime created_at) {
        this.blog_id = blog_id;
        this.title = title;
        this.blogger = blogger;
        this.description = description;
        this.created_at = created_at;
    }

    public Blogs() {}

    public int getBlog_id() {
        return blog_id;
    }

    public void setBlog_id(int blog_id) {
        this.blog_id = blog_id;
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

    public LocalDateTime getCreated_at() {
        return created_at;
    }
}
