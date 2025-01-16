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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team4.nottumblr.dto.ReblogsDTO;
import com.team4.nottumblr.model.Reblogs;
import com.team4.nottumblr.service.ReblogsService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/reblogs")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class ReblogsController {
    @Autowired
    private ReblogsService reblogsService;

    @GetMapping("/all")
    public ResponseEntity<List<ReblogsDTO>> getAllReblogs() {
        List<ReblogsDTO> allReblogs = reblogsService.getAllReblogs();
        return ResponseEntity.ok(allReblogs);
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<?> getAllReblogsByPostId(@PathVariable int postId) {
        List<ReblogsDTO> reblogs = reblogsService.getAllReblogsByPostId(postId);
        return ResponseEntity.ok(reblogs);
    }

    @PostMapping("/posts/{postId}")
    public ResponseEntity<?> createReblog(@CookieValue(name = "jwt") String token,
            @PathVariable int postId,
            @RequestBody Reblogs reblogRequest) {
        ReblogsDTO reblog = reblogsService.createReblog(postId, reblogRequest.getComment(), token);
        return ResponseEntity.status(201).body(reblog);
    }

    @DeleteMapping("/{reblogId}")
    public ResponseEntity<?> deleteReblog(@CookieValue(name = "jwt") String token,
            @PathVariable int reblogId) {
        reblogsService.deleteReblog(reblogId, token);
        return ResponseEntity.ok("Reblog deleted successfully.");
    }

    @GetMapping("/my-reblogs")
    public ResponseEntity<?> getMyReblogs(
            @CookieValue(name = "jwt", required = false) String token,
            @RequestHeader(value = "Authorization", required = false) String authHeader, HttpServletResponse response) {
        // Fallback to Authorization header if token is not in cookies
        if (token == null && authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // Extract token from "Bearer <token>"
        }

        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid JWT token");
        }

        try {
            List<ReblogsDTO> reblogs = reblogsService.getAllReblogsByBlogger(token);
            response.addHeader("Authorization", "Bearer " + token);
            return ResponseEntity.ok(reblogs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while fetching reblogs: " + e.getMessage());
        }
    }

    @GetMapping("/user/{bloggerId}")
    public ResponseEntity<?> getReblogsByBlogger(@PathVariable Long bloggerId) {
        List<ReblogsDTO> reblogs = reblogsService.getAllReblogsByOtherBlogger(bloggerId);
        return ResponseEntity.ok(reblogs);
    }
}