package com.team4.nottumblr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.team4.nottumblr.model.Comments;
import com.team4.nottumblr.service.CommentsService;

@RestController
public class CommentsController {
    
    @Autowired
    private CommentsService commentsService;

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<?> getComments(@CookieValue(name = "jwt") String token, @PathVariable int postId) {
        return ResponseEntity.ok(commentsService.getAllCommentsByPost(postId));
    }

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<?> createComment(@CookieValue(name = "jwt") String token, @PathVariable int postId, @RequestBody Comments comment) {
        return ResponseEntity.ok(commentsService.createComment(comment));
    }

    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@CookieValue(name = "jwt") String token, @PathVariable int postId, @PathVariable int commentId) {
        commentsService.deleteComment(commentId, postId, token);
        return ResponseEntity.ok("Comment delete successfully.");
    }
}
