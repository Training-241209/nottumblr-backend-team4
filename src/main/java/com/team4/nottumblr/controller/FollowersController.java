package com.team4.nottumblr.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.team4.nottumblr.dto.BloggersFollowersDTO;
import com.team4.nottumblr.service.FollowersService;

@RestController
@RequestMapping("/followers")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class FollowersController {

    @Autowired
    private FollowersService followersService;

    @GetMapping("/{bloggerId}")
    public ResponseEntity<?> getAllFollowersForBlogger(@PathVariable long bloggerId) {
        List<Map<String, String>> followers = followersService.getAllFollowersForBlogger(bloggerId);
        return ResponseEntity.ok(followers);
    }
    
    @PostMapping("/follow/{followeeId}")
    public ResponseEntity<?> followBlogger(@PathVariable long followeeId, @CookieValue(name = "jwt") String token) {
        followersService.followBlogger(followeeId, token);
        return ResponseEntity.ok("Blogger followed!");
    }

    @DeleteMapping("/unfollow/{followeeId}")
    public ResponseEntity<?> unfollowBlogger(@PathVariable long followeeId, @CookieValue(name = "jwt") String token) {
        followersService.unfollowBlogger(followeeId, token);
        return ResponseEntity.ok("Blogger unfollowed!");
    }

    @GetMapping("/isFollowing")
    public ResponseEntity<?> isFollowing(
            @RequestParam long followerId,
            @RequestParam long followeeId) {
        boolean isFollowing = followersService.isFollowing(followerId, followeeId);
        return ResponseEntity.ok(isFollowing);
    }

    @GetMapping("/top-bloggers")
    public ResponseEntity<?> getTopBloggers(@RequestParam(defaultValue = "5") int limit) {
        List<BloggersFollowersDTO> topBloggers = followersService.getTopBloggers(limit);
        return ResponseEntity.ok(topBloggers);
    }
}
