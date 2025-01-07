package com.team4.nottumblr.model;

import jakarta.persistence.*;

@Entity
public class Followers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int follower_id;

    @ManyToOne
    @JoinColumn(name = "blogger_id", nullable = false) // Foreign key to Bloggers table
    private Bloggers blogger; // Who is following

    @ManyToOne
    @JoinColumn(name = "following_blog_id", nullable = false) // Foreign key to Blogs table
    private Blogs blog; // The blog being followed

    // Default Constructor
    public Followers() {}

    public Followers(Bloggers blogger, Blogs blog) {
        this.blogger = blogger;
        this.blog = blog;
    }

    public int getFollower_id() {
        return follower_id;
    }

    public Bloggers getBlogger() {
        return blogger;
    }

    public void setBlogger(Bloggers blogger) {
        this.blogger = blogger;
    }

    public Blogs getBlog() {
        return blog;
    }

    public void setBlog(Blogs blog) {
        this.blog = blog;
    }
}
