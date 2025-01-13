package com.team4.nottumblr.service;

import org.springframework.stereotype.Component;

import com.team4.nottumblr.dto.BloggersDTO;
import com.team4.nottumblr.model.Bloggers;

@Component
public class BloggerMapper {
    
    public BloggersDTO convertToBloggersDTO(Bloggers blogger) {
        return new BloggersDTO(
            blogger.getBloggerId(),
            blogger.getUsername(),
            blogger.getEmail(),
            blogger.getRole().getRoleName()
        );
    }
}

