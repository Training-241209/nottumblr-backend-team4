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
    private int reblogId;

    @ManyToOne
    @JoinColumn(name = "blogId") 
    private Blogs blog;

    @ManyToOne
    @JoinColumn(name = "bloggerId")
    private Bloggers blogger;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime rebloggedAt;

    @Column
    private String comment;

    public Reblogs(Blogs blog, Bloggers blogger, String comment) {
        this.blog = blog;
        this.blogger = blogger;
        this.comment = comment;
    }

    public Reblogs() {}

    public int getReblogId() {
        return reblogId;
    }


    public Blogs getBlog() {
        return blog;
    }

    public void setPost(Blogs blog) {
        this.blog = blog;
    }

    public Bloggers getBlogger() {
        return blogger;
    }

    public void setBlogger(Bloggers blogger) {
        this.blogger = blogger;
    }

    public LocalDateTime getRebloggedAt() {
        return rebloggedAt;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
