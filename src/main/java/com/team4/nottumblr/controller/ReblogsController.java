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

    @GetMapping("/posts/{post_id}/reblogs")
    public ResponseEntity<?> getAllReblogsByPost(@PathVariable int post_id) {
        List<Reblogs> reblogs = reblogsService.getAllReblogsByPost(post_id);

        return ResponseEntity.ok(reblogs);
    }

    @PostMapping("/posts/{post_id}/reblogs")
    public ResponseEntity<?> createReblog(@CookieValue(name = "jwt") String token, @PathVariable int post_id, @RequestBody Reblogs reblog) {
        return ResponseEntity.ok(reblogsService.createReblog(post_id, reblog.getComment(), token));
    }

    @DeleteMapping("/posts/{post_id}/reblogs/{reblog_id}")
    public ResponseEntity<?> deleteReblog(@CookieValue(name = "jwt") String token, @PathVariable int post_id, @PathVariable int reblog_id) {
        reblogsService.deleteReblog(reblog_id, token);
        return ResponseEntity.ok("Reblog deleted successfully.");
    }
}
