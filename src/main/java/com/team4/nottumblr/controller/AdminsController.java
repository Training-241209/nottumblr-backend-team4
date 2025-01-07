package com.team4.nottumblr.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team4.nottumblr.dto.BloggersDTO;
import com.team4.nottumblr.model.Bloggers;
import com.team4.nottumblr.service.AdminsService;

@RestController
@RequestMapping("/admin")
public class AdminsController {

    @Autowired
    private AdminsService adminService;

    @GetMapping("/bloggers")
    public ResponseEntity<?> getAllBloggers(@CookieValue(name = "jwt") String token) {
        List<BloggersDTO> bloggers = adminService.getAllBloggers(token);
        return ResponseEntity.ok().body(bloggers);
    }

    @PatchMapping("/bloggers/{id}")
    public ResponseEntity<?> promoteBlogger(@CookieValue(name = "jwt") String token, @PathVariable long blogger_id) {
        Bloggers promotedBlogger = adminService.promoteBloggerById(blogger_id, token);
        return ResponseEntity.ok().body(promotedBlogger);
    }

    @DeleteMapping("/bloggers/{id}")
    public ResponseEntity<?> deleteBlogger(@CookieValue(name = "jwt") String token, @PathVariable long blogger_id) {
        adminService.deleteBloggerById(blogger_id, token);
        return ResponseEntity.ok().body("User deleted.");
    }}
