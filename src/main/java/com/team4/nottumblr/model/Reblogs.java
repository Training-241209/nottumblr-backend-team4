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
public class Reblogs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reblogId;

    @ManyToOne
    @JoinColumn(name = "postId")
    private Posts post;

    @ManyToOne
    @JoinColumn(name = "bloggerId")
    private Bloggers blogger;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime rebloggedAt;

    @Column
    private String comment;

    @OneToMany(mappedBy = "reblog", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Comments> comments;

    @OneToMany(mappedBy = "reblog", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Likes> likes;

    public Reblogs(Posts post, Bloggers blogger, String comment) {
        this.post = post;
        this.blogger = blogger;
        this.comment = comment;
    }

    public Reblogs() {}

    public int getReblogId() {
        return reblogId;
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

    public LocalDateTime getRebloggedAt() {
        return rebloggedAt;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<Comments> getComments() {
        return comments;
    }

    public void setComments(List<Comments> comments) {
        this.comments = comments;
    }

    public List<Likes> getLikes() {
        return likes;
    }

    public void setLikes(List<Likes> likes) {
        this.likes = likes;
    }
}
