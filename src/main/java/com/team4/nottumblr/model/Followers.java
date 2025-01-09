package com.team4.nottumblr.model;

import jakarta.persistence.*;

@Entity
public class Followers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int followerId;

    @ManyToOne
    @JoinColumn(name = "bloggerId") // Foreign key to Bloggers table
    private Bloggers blogger; // Who is following

    @ManyToOne
    @JoinColumn(name = "followingBlogId") // Foreign key to Blogs table
    private Blogs blog; // The blog being followed

    // Default Constructor
    public Followers() {}

    public Followers(Bloggers blogger, Blogs blog) {
        this.blogger = blogger;
        this.blog = blog;
    }

    public int getFollowerId() {
        return followerId;
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
