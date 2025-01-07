package com.team4.nottumblr.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.team4.nottumblr.model.Posts;
import com.team4.nottumblr.service.PostsService;

@RestController
@RequestMapping("/posts")
public class PostsController {

    @Autowired
    private PostsService postsService;

    @GetMapping
    public ResponseEntity<?> getAllPosts(@CookieValue(name = "jwt") String token,
            @RequestParam(required = false) Integer blog_id,
            @RequestParam(required = false) Long blogger_id) {
                List<Posts> posts = postsService.getAllPosts(token, blog_id, blogger_id);
                return ResponseEntity.ok(posts);
    }

    @GetMapping("/{post_id}")
    public ResponseEntity<?> getPostById(@CookieValue(name = "jwt") String token, @PathVariable int post_id) {
        Posts post = postsService.getPostById(post_id, token);
        return ResponseEntity.ok(post);
    }

    @PutMapping("/create")
    public ResponseEntity<?> createPost(@CookieValue(name = "jwt") String token, @RequestBody Posts post) {
        Posts createdPost = postsService.createPost(post, token);
        return ResponseEntity.ok(createdPost);
    }

    @PatchMapping("/update/{post_id}")
    public ResponseEntity<?> updatePost(@CookieValue(name = "jwt") String token, @PathVariable int post_id, @RequestBody Posts updatedPost) {
        Posts post = postsService.updatePost(post_id, updatedPost, token);
        return ResponseEntity.ok(post);
    }

    @DeleteMapping("/delete/{post_id}")
    public ResponseEntity<?> deletePost(@CookieValue(name = "jwt") String token, @PathVariable int post_id) {
        postsService.deletePost(post_id, token);
        return ResponseEntity.ok().body("Post deleted.");
    }
}
