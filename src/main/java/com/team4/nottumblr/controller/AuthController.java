package com.team4.nottumblr.controller;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team4.nottumblr.config.CookieConfig;
import com.team4.nottumblr.dto.BloggersDTO;
import com.team4.nottumblr.dto.JwtResponseDTO;
import com.team4.nottumblr.dto.LoginRequestDTO;
import com.team4.nottumblr.model.Bloggers;
import com.team4.nottumblr.service.AuthService;
import com.team4.nottumblr.service.JwtService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://18.222.20.24/", allowCredentials = "true")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private CookieConfig cookieConfig;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@RequestBody LoginRequestDTO loginRequest, HttpServletResponse response) {
        // Authenticate the user and generate a token
        JwtResponseDTO tokenResponse = authService.authenticateBlogger(loginRequest.getUsername(), loginRequest.getPassword());
    
        // Create the JWT cookie using the existing utility
        ResponseCookie jwtCookie = cookieConfig.createJwtCookie(tokenResponse.getToken());
    
        // Add the cookie to the response
        response.addHeader("Set-Cookie", jwtCookie.toString());
    
        // Return the token response
        return ResponseEntity.ok(tokenResponse);
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
    public ResponseEntity<BloggersDTO> getCurrentUser(HttpServletRequest request, HttpServletResponse response) {
        String token = null;
    
        // Retrieve the JWT token from the Authorization header
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // Extract the token after "Bearer "
        }
    
        // If no token is found in the Authorization header, check cookies
        if (token == null || token.isEmpty()) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                token = Arrays.stream(cookies)
                        .filter(c -> "jwt".equals(c.getName()))
                        .map(Cookie::getValue)
                        .findFirst()
                        .orElse(null);
            }
        }
    
        // Log the retrieved token
        System.out.println("JWT token received: " + token);
    
        // If no token is present, return unauthorized
        if (token == null || token.isEmpty()) {
            System.out.println("No JWT token found in cookies or Authorization header");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    
        try {
            // Decode and validate the token to get the user information
            BloggersDTO bloggerDTO = authService.getBlogger(token);
    
            // Refresh the token if it's close to expiry
            if (jwtService.isCloseToExpiry(token)) {
                System.out.println("Token is close to expiry, refreshing...");
                Bloggers blogger = authService.getBloggerEntity(token);
                String newToken = jwtService.generateToken(blogger);
    
                ResponseCookie jwtCookie = cookieConfig.createJwtCookie(newToken);
                response.addHeader("Set-Cookie", jwtCookie.toString());
                System.out.println("New JWT token issued and cookie set.");
            }
    
            return ResponseEntity.ok(bloggerDTO);
        } catch (IllegalArgumentException e) {
            System.out.println("Error validating token: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
    
    
    
    
}
