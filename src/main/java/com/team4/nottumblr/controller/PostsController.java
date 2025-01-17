package com.team4.nottumblr.controller;

import java.util.List;
import java.util.stream.Collectors;

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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team4.nottumblr.dto.PostsDTO;
import com.team4.nottumblr.dto.TrendingPostsDTO;
import com.team4.nottumblr.service.PostsService;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;

import org.yaml.snakeyaml.nodes.Tag;

@RestController
@RequestMapping("/posts")
@CrossOrigin(origins = "http://18.222.20.24/", allowCredentials = "true")
public class PostsController {

    @Autowired
    private PostsService postsService;

    @GetMapping("/all")
    public ResponseEntity<List<PostsDTO>> getAllPosts() {
        List<PostsDTO> allPosts = postsService.getAllPosts();
        return ResponseEntity.ok(allPosts);
    }

    @GetMapping("/my-posts")
    public ResponseEntity<?> getMyPosts(
            @CookieValue(name = "jwt", required = false) String token,
            @RequestHeader(value = "Authorization", required = false) String authHeader, HttpServletResponse response) {
        // Fallback to Authorization header if token is not in cookies
        if (token == null && authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // Extract token from "Bearer <token>"
        }

        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid JWT token");
        }

        response.addHeader("Authorization", "Bearer " + token);

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
            @CookieValue(name = "jwt", required = false) String token,
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody PostsDTO post) {
        // Retrieve token from header if not present in cookies
        if (token == null && authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }

        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: Token missing");
        }

        try {
            PostsDTO createdPost = postsService.createPost(post, token);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating post: " + e.getMessage());
        }
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(
            @CookieValue(name = "jwt", required = false) String token,
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable int postId) {
        if (token == null && authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }

        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: Token missing");
        }

        try {
            postsService.deletePost(postId, token);
            return ResponseEntity.ok().body("Post deleted");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting post: " + e.getMessage());
        }
    }

    @GetMapping("/user/{bloggerId}")
    public ResponseEntity<?> getPostsByBlogger(@PathVariable Long bloggerId) {
        List<PostsDTO> posts = postsService.getPostsByBlogger(bloggerId);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/tags/{tag}")
    public ResponseEntity<?> getTags(
            @CookieValue(name = "jwt", required = false) String token,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (token == null && authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }

        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: Token missing");
        }

        try {
            List<PostsDTO> tag = postsService.getPostByTag(token);
            return ResponseEntity.ok(tag);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching tags: " + e.getMessage());
        }
    }

    @GetMapping("/trending")
    public ResponseEntity<?> getTrendingPostNoAuth() {
        try {
            TrendingPostsDTO trending = postsService.getTrendingPostNoAuth();
            if (trending == null) {
                return ResponseEntity.ok("No posts found or no interactions yet.");
            }
            return ResponseEntity.ok(trending);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching trending post: " + e.getMessage());
        }
    }

}
