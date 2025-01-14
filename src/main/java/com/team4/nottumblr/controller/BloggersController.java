package com.team4.nottumblr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.team4.nottumblr.dto.BloggersDTO;
import com.team4.nottumblr.dto.ProfilePictureDTO;
import com.team4.nottumblr.model.Bloggers;
import com.team4.nottumblr.service.BloggerMapper;
import com.team4.nottumblr.service.BloggersService;

@RestController
@RequestMapping("/bloggers")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class BloggersController {

    @Autowired
    private BloggersService bloggersService;

    @Autowired
    private BloggerMapper bloggerMapper;

    @GetMapping("/{bloggerId}")
    public ResponseEntity<?> getBlogger(@CookieValue(name = "jwt") String token, @PathVariable long bloggerId) {
        Bloggers blogger = bloggersService.getBloggerById(bloggerId);
        BloggersDTO bloggerDTO = bloggerMapper.convertToBloggersDTO(blogger);
        return ResponseEntity.ok().body(bloggerDTO);
    }

    // Update Password
    @PutMapping("/{bloggerId}/password")
    public ResponseEntity<?> updatePassword(
            @CookieValue(name = "jwt") String token,
            @PathVariable long bloggerId,
            @RequestParam String newPassword) {
        bloggersService.updatePassword(token, newPassword);
        return ResponseEntity.ok().body("Password updated successfully for Blogger ID: " + bloggerId);
    }

    // Update First Name
    @PutMapping("/{bloggerId}/first-name")
    public ResponseEntity<?> updateFirstName(
            @CookieValue(name = "jwt") String token,
            @PathVariable long bloggerId,
            @RequestParam String newFirstName) {
        Bloggers updatedBlogger = bloggersService.updateFirstName(bloggerId, newFirstName);
        BloggersDTO bloggerDTO = bloggerMapper.convertToBloggersDTO(updatedBlogger);
        return ResponseEntity.ok().body(bloggerDTO);
    }

    // Update Last Name
    @PutMapping("/{bloggerId}/last-name")
    public ResponseEntity<?> updateLastName(
            @CookieValue(name = "jwt") String token,
            @PathVariable long bloggerId,
            @RequestParam String newLastName) {
        Bloggers updatedBlogger = bloggersService.updateLastName(bloggerId, newLastName);
        BloggersDTO bloggerDTO = bloggerMapper.convertToBloggersDTO(updatedBlogger);
        return ResponseEntity.ok().body(bloggerDTO);
    }

    // Delete Account
    @DeleteMapping("/{bloggerId}")
    public ResponseEntity<?> deleteAccount(
            @CookieValue(name = "jwt") String token,
            @PathVariable long bloggerId) {
        bloggersService.deleteAccount(bloggerId);
        return ResponseEntity.ok().body("Blogger account with ID: " + bloggerId + " has been deleted.");
    }

    @PutMapping("/profile-picture")
    public ResponseEntity<?> updateProfilePicture(
            @CookieValue(name = "jwt") String token,
            @RequestBody ProfilePictureDTO dto) {
        try {
            Bloggers updatedBlogger = bloggersService.updateProfilePicture(
                    token,
                    dto.getProfilePictureUrl());
            BloggersDTO bloggerDTO = bloggerMapper.convertToBloggersDTO(updatedBlogger);
            return ResponseEntity.ok().body(bloggerDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
