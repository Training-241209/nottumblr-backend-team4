package com.team4.nottumblr.model;

import jakarta.persistence.*;

@Entity
public class Followers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int followerId;

    @ManyToOne
    @JoinColumn(name = "follower_blogger_Id") // The blogger who is following
    private Bloggers follower;

    @ManyToOne
    @JoinColumn(name = "followee_blogger_Id") // The blogger who is being followed
    private Bloggers followee;

    // Default Constructor
    public Followers() {}

    // Parameterized Constructor
    public Followers(Bloggers follower, Bloggers followee) {
        this.follower = follower;
        this.followee = followee;
    }

    // Getter and Setter for followerId
    public int getFollowerId() {
        return followerId;
    }

    public void setFollowerId(int followerId) {
        this.followerId = followerId;
    }

    // Getter and Setter for follower
    public Bloggers getFollower() {
        return follower;
    }

    public void setFollower(Bloggers follower) {
        this.follower = follower;
    }

    // Getter and Setter for followee
    public Bloggers getFollowee() {
        return followee;
    }

    public void setFollowee(Bloggers followee) {
        this.followee = followee;
    }
}
