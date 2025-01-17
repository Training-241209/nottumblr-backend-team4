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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
@CrossOrigin(origins = "http://3.15.203.66:81", allowCredentials = "true")
public class BloggersController {

    @Autowired
    private BloggersService bloggersService;

    @Autowired
    private BloggerMapper bloggerMapper;

    @GetMapping("/{bloggerId}")
    public ResponseEntity<?> getBlogger(
            @CookieValue(name = "jwt", required = false) String token,
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable long bloggerId) {

        token = getTokenFromCookieOrHeader(token, authHeader);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid JWT token");
        }

        Bloggers blogger = bloggersService.getBloggerById(bloggerId);
        BloggersDTO bloggerDTO = bloggerMapper.convertToBloggersDTO(blogger);
        return ResponseEntity.ok().body(bloggerDTO);
    }

    @GetMapping("/profile/{username}")
    public ResponseEntity<?> getBloggerByUsername(@PathVariable String username) {
        Bloggers blogger = bloggersService.getBloggerByUsername(username);
        BloggersDTO bloggerDTO = bloggerMapper.convertToBloggersDTO(blogger);
        return ResponseEntity.ok().body(bloggerDTO);
    }

    @PutMapping("/{bloggerId}/password")
    public ResponseEntity<?> updatePassword(
            @CookieValue(name = "jwt", required = false) String token,
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable long bloggerId,
            @RequestParam String newPassword) {

        token = getTokenFromCookieOrHeader(token, authHeader);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid JWT token");
        }

        bloggersService.updatePassword(token, newPassword);
        return ResponseEntity.ok().body("Password updated successfully for Blogger ID: " + bloggerId);
    }

    @PutMapping("/{bloggerId}/first-name")
    public ResponseEntity<?> updateFirstName(
            @CookieValue(name = "jwt", required = false) String token,
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable long bloggerId,
            @RequestParam String newFirstName) {

        token = getTokenFromCookieOrHeader(token, authHeader);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid JWT token");
        }

        Bloggers updatedBlogger = bloggersService.updateFirstName(bloggerId, newFirstName);
        BloggersDTO bloggerDTO = bloggerMapper.convertToBloggersDTO(updatedBlogger);
        return ResponseEntity.ok().body(bloggerDTO);
    }

    @PutMapping("/{bloggerId}/last-name")
    public ResponseEntity<?> updateLastName(
            @CookieValue(name = "jwt", required = false) String token,
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable long bloggerId,
            @RequestParam String newLastName) {

        token = getTokenFromCookieOrHeader(token, authHeader);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid JWT token");
        }

        Bloggers updatedBlogger = bloggersService.updateLastName(bloggerId, newLastName);
        BloggersDTO bloggerDTO = bloggerMapper.convertToBloggersDTO(updatedBlogger);
        return ResponseEntity.ok().body(bloggerDTO);
    }

    @DeleteMapping("/{bloggerId}")
    public ResponseEntity<?> deleteAccount(
            @CookieValue(name = "jwt", required = false) String token,
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable long bloggerId) {

        token = getTokenFromCookieOrHeader(token, authHeader);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid JWT token");
        }

        bloggersService.deleteAccount(bloggerId);
        return ResponseEntity.ok().body("Blogger account with ID: " + bloggerId + " has been deleted.");
    }

    @PutMapping("/profile-picture")
    public ResponseEntity<?> updateProfilePicture(
            @CookieValue(name = "jwt", required = false) String token,
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody ProfilePictureDTO dto) {

        token = getTokenFromCookieOrHeader(token, authHeader);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid JWT token");
        }

        try {
            Bloggers updatedBlogger = bloggersService.updateProfilePicture(token, dto.getProfilePictureUrl());
            BloggersDTO bloggerDTO = bloggerMapper.convertToBloggersDTO(updatedBlogger);
            return ResponseEntity.ok().body(bloggerDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchBloggers(@RequestParam(required = false) String searchTerm) {
        List<BloggersDTO> bloggers = bloggersService.searchBloggers(searchTerm);
        return ResponseEntity.ok().body(bloggers);
    }

    private String getTokenFromCookieOrHeader(String token, String authHeader) {
        if (token == null && authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7); // Extract token from "Bearer <token>"
        }
        return token;
    }
}
