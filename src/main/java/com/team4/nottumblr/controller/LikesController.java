package com.team4.nottumblr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team4.nottumblr.dto.LikesDTO;
import com.team4.nottumblr.service.LikesService;

@RestController
@RequestMapping("/posts")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class LikesController {

    @Autowired
    private LikesService likesService;

    // Get all likes for a post
    @GetMapping("/{postId}/likes")
    public ResponseEntity<?> getLikesForPost(@PathVariable int postId) {
        return ResponseEntity.ok(likesService.getAllLikesByPost(postId));
    }

    // Like a post
    @PostMapping("/{postId}/likes/like")
    public ResponseEntity<?> createLikeForPost(@CookieValue(name = "jwt") String token, @PathVariable int postId) {
        LikesDTO likeResponse = likesService.createLikeForPost(postId, token);
        return ResponseEntity.status(HttpStatus.CREATED).body(likeResponse);
    }

    // Delete like for a post
    @DeleteMapping("/{postId}/likes/{likeId}")
    public ResponseEntity<?> deleteLikeForPost(@CookieValue(name = "jwt") String token, @PathVariable int postId, @PathVariable int likeId) {
        likesService.deleteLikeForPost(postId, likeId, token);
        return ResponseEntity.ok("Like deleted successfully.");
    }

    // Get all likes for a reblog
    @GetMapping("/reblogs/{reblogId}/likes")
    public ResponseEntity<?> getLikesForReblog(@PathVariable int reblogId) {
        return ResponseEntity.ok(likesService.getAllLikesByReblog(reblogId));
    }

    // Like a reblog
    @PostMapping("/reblogs/{reblogId}/likes/like")
    public ResponseEntity<?> createLikeForReblog(@CookieValue(name = "jwt") String token, @PathVariable int reblogId) {
        LikesDTO likeResponse = likesService.createLikeForReblog(reblogId, token);
        return ResponseEntity.status(HttpStatus.CREATED).body(likeResponse);
    }

    // Delete like for a reblog
    @DeleteMapping("/reblogs/{reblogId}/likes/{likeId}")
    public ResponseEntity<?> deleteLikeForReblog(@CookieValue(name = "jwt") String token, @PathVariable int reblogId, @PathVariable int likeId) {
        likesService.deleteLikeForReblog(reblogId, likeId, token);
        return ResponseEntity.ok("Like deleted successfully.");
    }
}
