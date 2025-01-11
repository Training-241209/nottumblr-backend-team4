package com.team4.nottumblr.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.team4.nottumblr.service.FollowersService;

@RestController
@RequestMapping("/followers")
public class FollowersController {

    @Autowired
    private FollowersService followersService;

    @GetMapping("/{bloggerId}")
    public ResponseEntity<?> getAllFollowersForBlogger(@PathVariable long bloggerId) {
        return ResponseEntity.ok(followersService.getAllFollowersForBlogger(bloggerId));
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
        @RequestParam long followeeId
    ) {
        boolean isFollowing = followersService.isFollowing(followerId, followeeId);
        return ResponseEntity.ok(isFollowing);
    }
}
