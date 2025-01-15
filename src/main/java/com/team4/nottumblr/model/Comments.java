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
public class Comments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int commentId;

    private String content;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "bloggerId")
    private Bloggers blogger;

    @ManyToOne
    @JoinColumn(name = "postId", nullable = true)
    private Posts post;

    @ManyToOne
    @JoinColumn(name = "reblogId", nullable = true)
    private Reblogs reblog;

    public Comments(String content, Bloggers blogger, Posts post, Reblogs reblog) {
        this.content = content;
        this.blogger = blogger;
        this.post = post;
        this.reblog = reblog;
    }

    public Comments() {}

    public int getCommentId() {
        return commentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Bloggers getBlogger() {
        return blogger;
    }

    public void setBlogger(Bloggers blogger) {
        this.blogger = blogger;
    }

    public Posts getPost() {
        return post;
    }

    public void setPost(Posts post) {
        this.post = post;
    }

    public Reblogs getReblog() {
        return reblog;
    }

    public void setReblog(Reblogs reblog) {
        this.reblog = reblog;
    }
}
