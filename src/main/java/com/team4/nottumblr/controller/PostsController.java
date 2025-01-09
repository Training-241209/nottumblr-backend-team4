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
            @RequestParam(required = false) Integer blogId,
            @RequestParam(required = false) Long bloggerId) {
                List<Posts> posts = postsService.getAllPosts(token, blogId, bloggerId);
                return ResponseEntity.ok(posts);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> getPostById(@CookieValue(name = "jwt") String token, @PathVariable int postId) {
        Posts post = postsService.getPostById(postId, token);
        return ResponseEntity.ok(post);
    }

    @PutMapping("/create")
    public ResponseEntity<?> createPost(@CookieValue(name = "jwt") String token, @RequestBody Posts post) {
        Posts createdPost = postsService.createPost(post, token);
        return ResponseEntity.ok(createdPost);
    }

    @PatchMapping("/update/{postId}")
    public ResponseEntity<?> updatePost(@CookieValue(name = "jwt") String token, @PathVariable int postId, @RequestBody Posts updatedPost) {
        Posts post = postsService.updatePost(postId, updatedPost, token);
        return ResponseEntity.ok(post);
    }

    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<?> deletePost(@CookieValue(name = "jwt") String token, @PathVariable int postId) {
        postsService.deletePost(postId, token);
        return ResponseEntity.ok().body("Post deleted.");
    }
}
