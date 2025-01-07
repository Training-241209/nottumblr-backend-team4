package com.team4.nottumblr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team4.nottumblr.dto.BloggersDTO;
import com.team4.nottumblr.dto.JwtResponseDTO;
import com.team4.nottumblr.dto.LoginRequestDTO;
import com.team4.nottumblr.model.Bloggers;
import com.team4.nottumblr.service.AuthService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest, HttpServletResponse response) {
        JwtResponseDTO token = authService.authenticateBlogger(loginRequest.getUsername(), loginRequest.getPassword());
        
        Cookie jwtCookie = new Cookie("jwt", token.getToken());
        jwtCookie.setPath("/");
        jwtCookie.setHttpOnly(true);
        response.addCookie(jwtCookie);

        return ResponseEntity.ok().body(token); 
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie jwtCookie = new Cookie("jwt", "");
        jwtCookie.setMaxAge(0);
        jwtCookie.setPath("/");
        jwtCookie.setHttpOnly(true);
        response.addCookie(jwtCookie);

        return ResponseEntity.ok().body("Logged out successfully."); 
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Bloggers blogger) {
        authService.registerBlogger(blogger);
        return ResponseEntity.status(HttpStatus.CREATED).body("Blogger registered successfully.");
    }

    @GetMapping("/me")
        public ResponseEntity<?> getCurrentUser(@CookieValue(name = "jwt", required = false) String token) {

        BloggersDTO blogger = authService.getBlogger(token);

        return ResponseEntity.ok().body(blogger);
    }
}
