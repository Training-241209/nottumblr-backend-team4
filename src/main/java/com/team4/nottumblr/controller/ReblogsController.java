package com.team4.nottumblr.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.team4.nottumblr.model.Reblogs;
import com.team4.nottumblr.service.ReblogsService;

@RestController
public class ReblogsController {

    @Autowired
    private ReblogsService reblogsService;

    @GetMapping("/posts/{postId}/reblogs")
    public ResponseEntity<?> getAllReblogsByPost(@PathVariable int postId) {
        List<Reblogs> reblogs = reblogsService.getAllReblogsByPost(postId);

        return ResponseEntity.ok(reblogs);
    }

    @PostMapping("/posts/{postId}/reblogs")
    public ResponseEntity<?> createReblog(@CookieValue(name = "jwt") String token, @PathVariable int postId, @RequestBody Reblogs reblog) {
        return ResponseEntity.ok(reblogsService.createReblog(postId, reblog.getComment(), token));
    }

    @DeleteMapping("/posts/{postId}/reblogs/{reblogId}")
    public ResponseEntity<?> deleteReblog(@CookieValue(name = "jwt") String token, @PathVariable int postId, @PathVariable int reblogId) {
        reblogsService.deleteReblog(reblogId, token);
        return ResponseEntity.ok("Reblog deleted successfully.");
    }
}
