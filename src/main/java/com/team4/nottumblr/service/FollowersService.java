package com.team4.nottumblr.service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team4.nottumblr.model.Bloggers;
import com.team4.nottumblr.model.Followers;
import com.team4.nottumblr.repository.BloggersRepository;
import com.team4.nottumblr.repository.FollowersRepository;

@Service
public class FollowersService {

    @Autowired
    private FollowersRepository followersRepository;

    @Autowired
    private BloggersRepository bloggersRepository;


    @Autowired
    private JwtService jwtService;

    public Map<String, List<String>> getAllFollowersForBlogger(long bloggerId) {
        List<Followers> followers = followersRepository.findByFollowee_BloggerId(bloggerId);
    
        // Extract usernames of followers
        List<String> followerUsernames = followers.stream()
            .map(follower -> follower.getFollower().getUsername())
            .toList();
    
        // Prepare the response as a map
        Map<String, List<String>> response = new HashMap<>();
        response.put("followers", followerUsernames);
    
        return response;
    }

    public Followers followBlogger(long bloggerId, String token) {
        Bloggers currentBlogger = jwtService.decodeToken(token);
    
        if (currentBlogger.getBloggerId() == bloggerId) {
            throw new IllegalArgumentException("You cannot follow yourself.");
        }
    
        // Fetch the followee (blogger being followed)
        Bloggers followee = bloggersRepository.findById(bloggerId)
                .orElseThrow(() -> new IllegalArgumentException("Blogger not found with ID: " + bloggerId));
    
        // Check if already following
        if (followersRepository.existsByFollower_BloggerIdAndFollowee_BloggerId(currentBlogger.getBloggerId(), bloggerId)) {
            throw new IllegalArgumentException("You are already following this blogger.");
        }
    
        Followers newFollower = new Followers(currentBlogger, followee);
        return followersRepository.save(newFollower);
    }
    
    

    public void unfollowBlogger(long bloggerId, String token) {
        Bloggers currentBlogger = jwtService.decodeToken(token);
    
        Followers follower = followersRepository.findByFollowerAndFollowee(
                currentBlogger.getBloggerId(), bloggerId)
                .orElseThrow(() -> new IllegalArgumentException("You are not following this blogger."));
    
        followersRepository.delete(follower);
    }
    

    public boolean isFollowing(long followerId, long followeeId) {
        return followersRepository.existsByFollower_BloggerIdAndFollowee_BloggerId(followerId, followeeId);
    }

}

