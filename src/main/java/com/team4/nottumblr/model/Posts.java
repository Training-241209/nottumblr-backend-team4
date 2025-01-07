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
public class Posts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int post_id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String media_url;

    @Column(nullable = false)
    private String media_type;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime created_at;

    @ManyToOne
    @JoinColumn(name = "blog_id", nullable = false)
    private Blogs blog;

    public Posts() {}

    public Posts(String content, String media_url, String media_type, Blogs blog) {
        this.content = content;
        this.media_url = media_url;
        this.media_type = media_type;
        this.blog = blog;
    }

    public int getPost_id() {
        return post_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMedia_url() {
        return media_url;
    }

    public void setMedia_url(String media_url) {
        this.media_url = media_url;
    }

    public String getMedia_type() {
        return media_type;
    }
    
    public void setMedia_type(String media_type) {
        this.media_type = media_type;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public Blogs getBlog() {
        return blog;
    }

    public void setBlog(Blogs blog) {
        this.blog = blog;
    }
}
