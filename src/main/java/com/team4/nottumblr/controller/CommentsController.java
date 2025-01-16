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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team4.nottumblr.dto.CommentsDTO;
import com.team4.nottumblr.model.Comments;
import com.team4.nottumblr.service.CommentsService;

@RestController
@RequestMapping
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class CommentsController {

    @Autowired
    private CommentsService commentsService;

    // Get comments for a specific post
    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<?> getCommentsByPost(@PathVariable int postId) {
        return ResponseEntity.ok(commentsService.getAllCommentsByPost(postId));
    }

    // Create a comment for a post
    @PostMapping("/posts/{postId}/comments/create")
    public ResponseEntity<?> createCommentForPost(
            @CookieValue(name = "jwt", required = false) String token,
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable int postId,
            @RequestBody Comments comment) {

        token = getTokenFromCookieOrHeader(token, authHeader);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid JWT token");
        }

        CommentsDTO createdComment = commentsService.createCommentForPost(postId, comment, token);
        return ResponseEntity.ok(createdComment);
    }

    // Delete a comment for a post
    @DeleteMapping("/posts/{postId}/comments/delete/{commentId}")
    public ResponseEntity<?> deleteCommentForPost(
            @CookieValue(name = "jwt", required = false) String token,
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable int postId,
            @PathVariable int commentId) {

        token = getTokenFromCookieOrHeader(token, authHeader);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid JWT token");
        }

        commentsService.deleteCommentByPost(commentId, postId, token);
        return ResponseEntity.ok("Comment deleted successfully.");
    }

    // Get comments for a specific reblog
    @GetMapping("/reblogs/{reblogId}/comments")
    public ResponseEntity<?> getCommentsByReblog(@PathVariable int reblogId) {
        return ResponseEntity.ok(commentsService.getAllCommentsByReblog(reblogId));
    }

    // Create a comment for a reblog
    @PostMapping("/reblogs/{reblogId}/comments/create")
    public ResponseEntity<?> createCommentForReblog(
            @CookieValue(name = "jwt", required = false) String token,
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable int reblogId,
            @RequestBody Comments comment) {

        token = getTokenFromCookieOrHeader(token, authHeader);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid JWT token");
        }

        CommentsDTO createdComment = commentsService.createCommentForReblog(reblogId, comment, token);
        return ResponseEntity.ok(createdComment);
    }

    // Delete a comment for a reblog
    @DeleteMapping("/reblogs/{reblogId}/comments/delete/{commentId}")
    public ResponseEntity<?> deleteCommentForReblog(
            @CookieValue(name = "jwt", required = false) String token,
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable int reblogId,
            @PathVariable int commentId) {

        token = getTokenFromCookieOrHeader(token, authHeader);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid JWT token");
        }

        commentsService.deleteCommentByReblog(commentId, reblogId, token);
        return ResponseEntity.ok("Comment deleted successfully.");
    }

    // Utility method to retrieve the token from either the cookie or the Authorization header
    private String getTokenFromCookieOrHeader(String token, String authHeader) {
        if (token == null && authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7); // Extract token from "Bearer <token>"
        }
        return token;
    }
}
