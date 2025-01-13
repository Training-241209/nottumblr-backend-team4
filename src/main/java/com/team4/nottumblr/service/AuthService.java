package com.team4.nottumblr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.team4.nottumblr.dto.BloggersDTO;
import com.team4.nottumblr.dto.JwtResponseDTO;
import com.team4.nottumblr.model.Bloggers;
import com.team4.nottumblr.model.Roles;
import com.team4.nottumblr.repository.BloggersRepository;
import com.team4.nottumblr.repository.RolesRepository;

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
    private RolesRepository rolesRepository;

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

        Roles role = rolesRepository.findByRoleName("User");
        blogger.setRole(role);

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

        long bloggerId = currentBlogger.getBloggerId();

        Bloggers blogger = bloggersRepository.findById(bloggerId)
                .orElseThrow(() -> new IllegalArgumentException("Blogger not found with ID: " + bloggerId));

        return bloggerMapper.convertToBloggersDTO(blogger);
    }
}