package com.team4.nottumblr.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Likes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int likeId;

    @ManyToOne
    @JoinColumn(name = "postId")
    private Posts post;

    @ManyToOne
    @JoinColumn(name = "bloggerId")
    private Bloggers blogger;

    public Likes(Posts post, Bloggers blogger) {
        this.post = post;
        this.blogger = blogger;
    }

    public Likes() {}

    public int getLikeId() {
        return likeId;
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
}
