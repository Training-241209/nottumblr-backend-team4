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

    @GetMapping("/posts/{post_id}/likes")
    public ResponseEntity<?> getLikes(@CookieValue(name = "jwt") String token, @PathVariable int post_id) {
        return ResponseEntity.ok(likesService.getAllLikesByPost(post_id));
    }

    @PostMapping("/posts/{post_id}/likes")
    public ResponseEntity<?> createLike(@CookieValue(name = "jwt") String token, @PathVariable int post_id) {
        return ResponseEntity.ok(likesService.createLike(post_id, token));
    }

    @DeleteMapping("/posts/{post_id}/likes/{like_id}")
    public ResponseEntity<?> deleteLike(@CookieValue(name = "jwt") String token, @PathVariable int post_id, @PathVariable int like_id) {
        likesService.deleteLike(like_id, token);
        return ResponseEntity.ok("Like deleted successfully.");
    }    
}
