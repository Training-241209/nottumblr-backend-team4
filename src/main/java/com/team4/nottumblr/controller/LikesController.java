package com.team4.nottumblr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team4.nottumblr.service.LikesService;

@RestController
public class LikesController {

    @Autowired
    private LikesService likesService;

    @GetMapping("/posts/{postId}/likes")
    public ResponseEntity<?> getLikes(@CookieValue(name = "jwt") String token, @PathVariable int postId) {
        return ResponseEntity.ok(likesService.getAllLikesByPost(postId));
    }

    @PostMapping("/posts/{postId}/likes")
    public ResponseEntity<?> createLike(@CookieValue(name = "jwt") String token, @PathVariable int postId) {
        return ResponseEntity.ok(likesService.createLike(postId, token));
    }

    @DeleteMapping("/posts/{postId}/likes/{likeId}")
    public ResponseEntity<?> deleteLike(@CookieValue(name = "jwt") String token, @PathVariable int postId, @PathVariable int likeId) {
        likesService.deleteLike(likeId, token);
        return ResponseEntity.ok("Like deleted successfully.");
    }    
}
