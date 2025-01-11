package com.team4.nottumblr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team4.nottumblr.dto.LikesDTO;
import com.team4.nottumblr.service.LikesService;

@RestController
@RequestMapping("/posts/{postId}/likes")
public class LikesController {

    @Autowired
    private LikesService likesService;

    @GetMapping
    public ResponseEntity<?> getLikes(@PathVariable int postId) {
        return ResponseEntity.ok(likesService.getAllLikesByPost(postId));
    }

    @PostMapping("/like")
    public ResponseEntity<?> createLike(@CookieValue(name = "jwt") String token, @PathVariable int postId) {
        LikesDTO likeResponse = likesService.createLike(postId, token);
        return ResponseEntity.status(HttpStatus.CREATED).body(likeResponse);
    }

    @DeleteMapping("/{likeId}")
    public ResponseEntity<?> deleteLike(@CookieValue(name = "jwt") String token, @PathVariable int postId, @PathVariable int likeId) {
        likesService.deleteLike(postId, likeId, token);
        return ResponseEntity.ok("Like deleted successfully.");
    }
}

