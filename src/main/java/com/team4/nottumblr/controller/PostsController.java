package com.team4.nottumblr.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
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
public class PostsController {

    @Autowired
    private PostsService postsService;

    @GetMapping("/blog/{blogId}")
    public ResponseEntity<?> getAllPostsFromBlog(@PathVariable int blogId) {
        List<PostsDTO> posts = postsService.getAllPostsFromBlog(blogId);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> getPostById(@PathVariable int postId) {
        PostsDTO post = postsService.getPostById(postId);
        return ResponseEntity.ok(post);
    }

    @PostMapping("/{blogId}")
    public ResponseEntity<?> createPost(@CookieValue(name = "jwt") String token, @PathVariable int blogId, @RequestBody PostsDTO post) {
        post.setBlogId(blogId); 
        PostsDTO createdPost = postsService.createPost(post, token);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@CookieValue(name = "jwt") String token, @PathVariable int postId) {
        postsService.deletePost(postId, token);
        return ResponseEntity.ok().body("Post deleted");
    }
}

