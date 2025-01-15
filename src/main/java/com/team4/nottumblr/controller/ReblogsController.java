package com.team4.nottumblr.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.team4.nottumblr.dto.ReblogsDTO;
import com.team4.nottumblr.model.Reblogs;
import com.team4.nottumblr.service.ReblogsService;

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
    public ResponseEntity<?> getMyReblogs(@CookieValue(name = "jwt") String token) {
        List<ReblogsDTO> reblogs = reblogsService.getAllReblogsByBlogger(token);
        return ResponseEntity.ok(reblogs);
    }

    @GetMapping("/user/{bloggerId}")
    public ResponseEntity<?> getReblogsByBlogger(@PathVariable Long bloggerId) {
        List<ReblogsDTO> reblogs = reblogsService.getAllReblogsByOtherBlogger(bloggerId);
        return ResponseEntity.ok(reblogs);
    }
}