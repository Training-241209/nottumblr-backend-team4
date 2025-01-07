package com.team4.nottumblr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.team4.nottumblr.dto.BloggersDTO;
import com.team4.nottumblr.dto.JwtResponseDTO;
import com.team4.nottumblr.model.Bloggers;
import com.team4.nottumblr.repository.BloggersRepository;

@Service
public class AuthService {
    
    @Value("${jwt.secret}")
    private String secretKey;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private BloggersRepository bloggersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private BloggerMapper bloggerMapper;

    public void registerBlogger(Bloggers blogger) {
        if (bloggersRepository.existsByUsername(blogger.getUsername())) {
            throw new IllegalArgumentException("Username already exists"); 
        }

        if (bloggersRepository.existsByEmail(blogger.getEmail())) {
            throw new IllegalArgumentException("Email already exists"); 
        }

        String hashedPassword = passwordEncoder.encode(blogger.getPassword());
        blogger.setPassword(hashedPassword);

        bloggersRepository.save(blogger);
    }

    public JwtResponseDTO authenticateBlogger(String username, String password) {
        Bloggers blogger = bloggersRepository.findByUsername(username);

        if (blogger == null || !passwordEncoder.matches(password, blogger.getPassword())) {
            throw new IllegalArgumentException("Invalid username or password"); 
        }

        String token = jwtService.generateToken(blogger);
        
        return new JwtResponseDTO(token);
    }

    public BloggersDTO getBlogger(String token) {
        Bloggers currentBlogger = jwtService.decodeToken(token);

        long blogger_id = currentBlogger.getBlogger_id();

        Bloggers blogger = bloggersRepository.findById(blogger_id).orElse(null);

        return bloggerMapper.convertToBloggersDTO(blogger);
    }
}