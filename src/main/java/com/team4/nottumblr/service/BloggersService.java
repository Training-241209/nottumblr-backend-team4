package com.team4.nottumblr.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team4.nottumblr.dto.BloggersDTO;
import com.team4.nottumblr.model.Bloggers;
import com.team4.nottumblr.model.Blogs;
import com.team4.nottumblr.repository.BloggersRepository;

@Service
public class BloggersService {

    @Autowired
    private BloggersRepository bloggersRepository;

    @Autowired
    private JwtService jwtService;


    public boolean existsBloggerByBloggerId(long blogger_id) {
        return bloggersRepository.existsById(blogger_id);
    }

    public Bloggers getBloggerById(long blogger_id) {
        if (!bloggersRepository.existsById(blogger_id)) {
            throw new IllegalArgumentException("Blogger with ID: " + blogger_id + " does not exist.");
        }
        return bloggersRepository.findById(blogger_id).orElse(null);
    }

    public Bloggers updateBlogger(long blogger_id, BloggersDTO bloggersDTO, String token) {
        Bloggers currentBlogger = jwtService.decodeToken(token);

        if (!currentBlogger.getRole().getRoleName().equals("ADMIN")) {
            throw new IllegalArgumentException("Only admins can update bloggers.");
        }

        Bloggers bloggerToUpdate = bloggersRepository.findById(blogger_id)
                .orElseThrow(() -> new IllegalArgumentException("Blogger with ID: " + blogger_id + " does not exist."));

        if (bloggersDTO.getUsername() != null && !bloggersDTO.getUsername().isEmpty()) {
            bloggerToUpdate.setUsername(bloggersDTO.getUsername());
        }

        return bloggersRepository.save(bloggerToUpdate);
    }

    public List<Blogs> getAllBlogsByBlogger(long blogger_id) {
        Bloggers blogger = bloggersRepository.findById(blogger_id)
                .orElseThrow(() -> new IllegalArgumentException("Blogger with ID: " + blogger_id + " not found"));
        return blogger.getBlogs();
    }

}
