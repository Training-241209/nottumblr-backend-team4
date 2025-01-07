package com.team4.nottumblr.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team4.nottumblr.model.Followers;
import com.team4.nottumblr.service.FollowersService;

@RestController
@RequestMapping("/followers")
public class FollowersController {
    @Autowired
    private FollowersService followersService;

    @GetMapping("/blogs/{blog_id}/followers")
    public ResponseEntity<?> getFollowersByBlogId(@CookieValue(name = "jwt") String token, @PathVariable int blog_id) {
        List<Followers> followers = followersService.getFollowersByBlogId(blog_id, token);
        return ResponseEntity.ok(followers);
    }

    @PostMapping("/blogs/{blog_id}/followers")
    public ResponseEntity<?> followBlog(@CookieValue(name = "jwt") String token, @PathVariable int blog_id) {
        followersService.followBlog(blog_id, token);
        return ResponseEntity.ok("Blog followed successfully.");
    }

    @DeleteMapping("/blogs/{blog_id}/followers")
    public ResponseEntity<?> unfollowBlog(@CookieValue(name = "jwt") String token, @PathVariable int blog_id) {
        followersService.unfollowBlog(blog_id, token);
        return ResponseEntity.ok("Blog unfollowed successfully.");
    }
}
