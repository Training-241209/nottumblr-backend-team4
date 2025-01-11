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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team4.nottumblr.dto.ReblogsDTO;
import com.team4.nottumblr.model.Reblogs;
import com.team4.nottumblr.service.ReblogsService;

@RestController
@RequestMapping("/reblogs")
public class ReblogsController {

    @Autowired
    private ReblogsService reblogsService;

    @GetMapping("/blogs/{blogId}")
    public ResponseEntity<?> getAllReblogsByBlogId(@PathVariable int blogId) {
        List<ReblogsDTO> reblogs = reblogsService.getAllReblogsByBlogId(blogId);
        return ResponseEntity.ok(reblogs);
    }

    @PostMapping("/blogs/{blogId}")
    public ResponseEntity<?> createReblog(@CookieValue(name = "jwt") String token, 
                                          @PathVariable int blogId, 
                                          @RequestBody Reblogs reblogRequest) {
        ReblogsDTO reblog = reblogsService.createReblog(blogId, reblogRequest.getComment(), token);
        return ResponseEntity.status(201).body(reblog);
    }

  
    @DeleteMapping("/{reblogId}")
    public ResponseEntity<?> deleteReblog(@CookieValue(name = "jwt") String token, 
                                          @PathVariable int reblogId) {

        reblogsService.deleteReblog(reblogId, token);
        return ResponseEntity.ok("Reblog deleted successfully.");
    }
}

