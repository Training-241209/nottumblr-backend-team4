package com.team4.nottumblr.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.team4.nottumblr.dto.BloggersDTO;
import com.team4.nottumblr.model.Bloggers;
import com.team4.nottumblr.repository.BloggersRepository;

import jakarta.transaction.Transactional;

@Service
public class BloggersService {

    @Autowired
    private BloggersRepository bloggersRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private BloggerMapper bloggerMapper;

    public boolean existsBloggerByBloggerId(long bloggerId) {
        return bloggersRepository.existsById(bloggerId);
    }

    public Bloggers getBloggerByUsername(String username) {
        return bloggersRepository.findByUsername(username);
    }

    public List<BloggersDTO> searchBloggers(String searchTerm) {
        List<Bloggers> bloggers;
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            bloggers = bloggersRepository.findAll();
        } else {
            bloggers = bloggersRepository.findByUsernameContainingIgnoreCase(searchTerm.trim());
        }
        
        return bloggers.stream()
                .map(this.bloggerMapper::convertToBloggersDTO)  
                .collect(Collectors.toList());
    }

    public Bloggers getBloggerById(long bloggerId) {
        if (!bloggersRepository.existsById(bloggerId)) {
            throw new IllegalArgumentException("Blogger with ID: " + bloggerId + " does not exist.");
        }
        return bloggersRepository.findById(bloggerId).orElse(null);
    }

    public Bloggers updatePassword(String token, String newPassword) {
        Bloggers currentBlogger = authService.getBloggerEntity(token);

        // Validation
        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
        if (newPassword.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long.");
        }

        // Password Hashing
        currentBlogger.setPassword(passwordEncoder.encode(newPassword));
        return bloggersRepository.save(currentBlogger);
    }

    public Bloggers updateFirstName(long bloggerId, String newFirstName) {
        Bloggers blogger = getBloggerById(bloggerId);
        blogger.setFirstName(newFirstName);
        return bloggersRepository.save(blogger);
    }

    public Bloggers updateLastName(long bloggerId, String newLastName) {
        Bloggers blogger = getBloggerById(bloggerId);
        blogger.setLastName(newLastName);
        return bloggersRepository.save(blogger);
    }

    public void deleteAccount(long bloggerId) {
        if (!bloggersRepository.existsById(bloggerId)) {
            throw new IllegalArgumentException("Blogger with ID: " + bloggerId + " does not exist.");
        }
        bloggersRepository.deleteById(bloggerId);
    }

    @Transactional
    public Bloggers updateProfilePicture(String token, String profilePictureUrl) {
        Bloggers currentBlogger = authService.getBloggerEntity(token);

        // Validation
        if (profilePictureUrl == null || profilePictureUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("Profile picture URL cannot be empty.");
        }

        // Update the profile picture URL
        currentBlogger.setProfilePictureUrl(profilePictureUrl);

        // Save and return the updated blogger with updated profile picture.
        return bloggersRepository.save(currentBlogger);
    }
}
