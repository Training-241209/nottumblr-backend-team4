package com.team4.nottumblr.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team4.nottumblr.model.Blogs;
import com.team4.nottumblr.service.BlogsService;

@RestController
@RequestMapping("/blogs")
public class BlogsController {
    
    @Autowired
    private BlogsService blogsService;

    @GetMapping
    public ResponseEntity<?> getAllBlogs(@CookieValue(name = "jwt") String token) {
        List<Blogs> blogs = blogsService.getAllBlogs(token);
        return ResponseEntity.ok(blogs);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createBlog(@RequestBody Blogs blog, @CookieValue(name = "jwt") String token) {
        Blogs createdBlog = blogsService.createBlog(blog, token);
        return ResponseEntity.ok(createdBlog);
    }

    @GetMapping("/{blogId}")
    public ResponseEntity<?> getBlogById(@CookieValue(name = "jwt") String token, @PathVariable int blogId) {
        Blogs blog = blogsService.getBlogById(blogId, token);
        return ResponseEntity.ok(blog);
    }

    @DeleteMapping("/delete/{blogId}")
    public ResponseEntity<?> deleteBlog(@CookieValue(name = "jwt") String token, @PathVariable int blogId) {
        blogsService.deleteBlog(blogId, token);
        return ResponseEntity.ok().body("Blog deleted.");
    }

    @PatchMapping("/update/{blogId}")
    public ResponseEntity<?> updateBlog(@CookieValue(name = "jwt") String token, @PathVariable int blogId, @RequestBody Blogs updatedBlog) {
        Blogs blog = blogsService.updateBlog(blogId, updatedBlog, token);
        return ResponseEntity.ok(blog);
    }
}
