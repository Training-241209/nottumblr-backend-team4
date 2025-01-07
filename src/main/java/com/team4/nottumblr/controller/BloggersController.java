package com.team4.nottumblr.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team4.nottumblr.dto.BloggersDTO;
import com.team4.nottumblr.model.Bloggers;
import com.team4.nottumblr.model.Blogs;
import com.team4.nottumblr.service.BloggerMapper;
import com.team4.nottumblr.service.BloggersService;

@RestController
@RequestMapping("/bloggers")
public class BloggersController {
    
    @Autowired
    private BloggersService bloggersService;

    @Autowired
    private BloggerMapper bloggerMapper;

    @GetMapping("/{blogger_id}")
    public ResponseEntity<?> getBlogger(@CookieValue(name = "jwt") String token, @PathVariable long blogger_id) {
        Bloggers blogger = bloggersService.getBloggerById(blogger_id);
        BloggersDTO bloggerDTO = bloggerMapper.convertToBloggersDTO(blogger);
        return ResponseEntity.ok().body(bloggerDTO);
    }

    @PatchMapping("/{blogger_id}")
    public ResponseEntity<?> updateBlogger(@PathVariable long blogger_id, @RequestBody BloggersDTO bloggersDTO, @RequestHeader("Authorization") String token) {
        bloggersService.updateBlogger(blogger_id, bloggersDTO, token);
        return ResponseEntity.ok("Blogger profile updated successfully.");
    }

    @GetMapping("/{blogger_id}/posts")
    public ResponseEntity<?> getBloggerPosts(@CookieValue(name = "jwt") String token, @PathVariable long blogger_id) {
        List<Blogs> blogs = bloggersService.getAllBlogsByBlogger(blogger_id);
        return ResponseEntity.ok(blogs);
    }

}
