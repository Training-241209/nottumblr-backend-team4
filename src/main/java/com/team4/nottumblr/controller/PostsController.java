package com.team4.nottumblr.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team4.nottumblr.dto.PostsDTO;
import com.team4.nottumblr.service.PostsService;

@RestController
@RequestMapping("/posts")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class PostsController {

    @Autowired
    private PostsService postsService;


    @GetMapping("/my-posts")
    public ResponseEntity<?> getMyPosts(@CookieValue(name = "jwt") String token) {
        List<PostsDTO> postsAndReblogs = postsService.getAllMyPosts(token);
        return ResponseEntity.ok(postsAndReblogs);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> getPostById(@PathVariable int postId) {
        PostsDTO post = postsService.getPostById(postId);
        return ResponseEntity.ok(post);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createPost(
        @CookieValue(name = "jwt") String token, 
        @RequestBody PostsDTO post
    ) {
        PostsDTO createdPost = postsService.createPost(post, token);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@CookieValue(name = "jwt") String token, @PathVariable int postId) {
        postsService.deletePost(postId, token);
        return ResponseEntity.ok().body("Post deleted");
    }
}

