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
public class Reblogs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reblog_id;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Posts post;

    @ManyToOne
    @JoinColumn(name = "blogger_id", nullable = false)
    private Bloggers blogger;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime reblogged_at;

    @Column
    private String comment;

    public Reblogs(Posts post, Bloggers blogger, String comment) {
        this.post = post;
        this.blogger = blogger;
        this.comment = comment;
    }

    public Reblogs() {}

    public int getReblog_id() {
        return reblog_id;
    }


    public Posts getPost() {
        return post;
    }

    public void setPost(Posts post) {
        this.post = post;
    }

    public Bloggers getBlogger() {
        return blogger;
    }

    public void setBlogger(Bloggers blogger) {
        this.blogger = blogger;
    }

    public LocalDateTime getReblogged_at() {
        return reblogged_at;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
